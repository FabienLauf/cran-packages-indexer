package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor.DescriptionToCranPackageProcessor;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor.NormalizeDescriptionProcessor;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor.PackageListingToUrlProcessor;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor.PersistCranPackageProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.tarfile.TarSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CranPackagesListRouteBuilder extends RouteBuilder {

    public static final String GET_PACKAGES_URL_LIST_ROUTE_ID = "getPackageUrlsList";
    public static final String GET_PACKAGES_URL_LIST_ROUTE_URI = "direct:"+GET_PACKAGES_URL_LIST_ROUTE_ID;
    public static final String DOWNLOAD_PACKAGE_ROUTE_ID = "downloadPackage";
    public static final String DOWNLOAD_PACKAGE_ROUTE_URI = "direct:"+DOWNLOAD_PACKAGE_ROUTE_ID;
    public static final String GET_PACKAGE_DESCRIPTION_ROUTE_ID = "getPackageDescription";
    public static final String GET_PACKAGE_DESCRIPTION_ROUTE_URI = "direct:"+GET_PACKAGE_DESCRIPTION_ROUTE_ID;
    public static final String PERSIST_CRAN_PACKAGE_ROUTE_ID = "persistCranPackage";
    public static final String PERSIST_CRAN_PACKAGE_ROUTE_URI = "direct:"+PERSIST_CRAN_PACKAGE_ROUTE_ID;

    private static final String PACKAGED_LIST_URL = "https://cran.r-project.org/src/contrib/PACKAGES";

    private Integer maxNbPackages;

    private PackageListingToUrlProcessor packageListingToUrlProcessor;
    private NormalizeDescriptionProcessor normalizeDescriptionProcessor;
    private DescriptionToCranPackageProcessor descriptionToCranPackageProcessor;
    private PersistCranPackageProcessor persistCranPackageProcessor;

    @Autowired
    public CranPackagesListRouteBuilder(@Value("${maxNbPackages}") Integer maxNbPackages, PackageListingToUrlProcessor packageListingToUrlProcessor, NormalizeDescriptionProcessor normalizeDescriptionProcessor, DescriptionToCranPackageProcessor descriptionToCranPackageProcessor, PersistCranPackageProcessor persistCranPackageProcessor) {
        this.maxNbPackages = maxNbPackages;
        this.packageListingToUrlProcessor = packageListingToUrlProcessor;
        this.normalizeDescriptionProcessor = normalizeDescriptionProcessor;
        this.descriptionToCranPackageProcessor = descriptionToCranPackageProcessor;
        this.persistCranPackageProcessor = persistCranPackageProcessor;
    }

    @Override
    public void configure() throws Exception {

        from(GET_PACKAGES_URL_LIST_ROUTE_URI).routeId(GET_PACKAGES_URL_LIST_ROUTE_ID)
            .log(">>> Please wait. Getting the list of all available packages from " + PACKAGED_LIST_URL)
            .to(PACKAGED_LIST_URL)
            .split(body().tokenize("\n\n"))
                .parallelProcessing().streaming()
                    .choice().when(exchangeProperty("CamelSplitIndex").isLessThan(maxNbPackages))
                        .process(packageListingToUrlProcessor)
                        .to(DOWNLOAD_PACKAGE_ROUTE_URI)
                    .endChoice()
                .end()
            .end()
            .log(">>> Cran Packages Indexing DONE <<<")
            .log("${headers}");

        from(DOWNLOAD_PACKAGE_ROUTE_URI).routeId(DOWNLOAD_PACKAGE_ROUTE_ID)
            .log("Downloading package ${body}")
            .toD("${body}?throwExceptionOnFailure=false")
            .choice().when(simple("${headers.CamelHttpResponseCode} < 300"))
                .to(GET_PACKAGE_DESCRIPTION_ROUTE_URI)
            .otherwise()
                .log("ERROR - ${headers.CamelHttpResponseCode} http response for file [${headers.CamelFileName}]")
            .end();

        from(GET_PACKAGE_DESCRIPTION_ROUTE_URI).routeId(GET_PACKAGE_DESCRIPTION_ROUTE_ID)
            .unmarshal().gzip()
            .split(new TarSplitter())
                .streaming()
                .choice().when(header("CamelTarFileEntryName").endsWith("DESCRIPTION"))
                    .process(normalizeDescriptionProcessor)
                    .process(descriptionToCranPackageProcessor)
                    .to(PERSIST_CRAN_PACKAGE_ROUTE_URI);

        from(PERSIST_CRAN_PACKAGE_ROUTE_URI).routeId(PERSIST_CRAN_PACKAGE_ROUTE_ID)
            .process(persistCranPackageProcessor)
            .log("CranPackage id[${body.id}] name[${body.name}] persisted in DB");

    }

}
