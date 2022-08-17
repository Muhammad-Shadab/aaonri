package com.aaonri.app.data.advertise.model

data class ModuleWiseTemplate(
    val listOfModule: List<ModuleTemplate>
)

data class ModuleTemplate(
    val moduleName: String,
    val templateLink: String,
    var isSelected: Boolean = false
)

val homeTemplates = ModuleWiseTemplate(
    listOf(
        ModuleTemplate(
            "Home",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/Dashboard%20Step%2001.png"
        ),
        ModuleTemplate(
            "Landing Page",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/Landing%20Page%20Step%2001.png"
        ),
        ModuleTemplate(
            "Product Details",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/Product%20Details%20Step%2001.png"
        )
    )
)

val landingPageLocationTemplate = ModuleWiseTemplate(
    listOf(
        ModuleTemplate(
            "Immigration just above footer image",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png"
        ),
        ModuleTemplate(
            "Immigration just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-text-only1.png"
        ),
        ModuleTemplate(
            "Immigration main section",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-LHS-main-section1.png"
        ),
        ModuleTemplate(
            "Immigration top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-top-banner1.png"
        ),
        ModuleTemplate(
            "Job just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-just-above-footer-image-only1.png"
        ),
        ModuleTemplate(
            "Job just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-just-above-footer-text-only1.png"
        ),
        ModuleTemplate(
            "Job LHS main section",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-LHS-main-section1.png"
        ),
        ModuleTemplate(
            "Job top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-top-banner1.png"
        ),
        ModuleTemplate(
            "Classified just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-just-above-footer-image-only1.png"
        ),
        ModuleTemplate(
            "Classified just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-just-above-footer-text-only1.png"
        ),
        ModuleTemplate(
            "Classified LHS main section",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-LHS-main-section1.png"
        ),
        ModuleTemplate(
            "Classified top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-top-banner1.png"
        ),

        ModuleTemplate(
            "Event just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-event-just-above-footer-image-only1.png"
        ),
        ModuleTemplate(
            "Event just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-event-just-above-footer-text-only1.png"
        ),
        ModuleTemplate(
            "Event LHS main section",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-LHS-of-main-section1.png"
        ),
        ModuleTemplate(
            "Event top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-top-baner1.png"
        )
    )
)

val productDetailsLocationTemplate = ModuleWiseTemplate(
    listOf(
        ModuleTemplate(
            "Job just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-just-above-footer-image-only1.png"
        ),
        ModuleTemplate(
            "Job just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-just-above-footer-text-only1.png"
        ),
        ModuleTemplate(
            "Job top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-top-banner1.png"
        ),
        ModuleTemplate(
            "Classified just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-just-above-footer-image-only1.png"
        ),
        ModuleTemplate(
            "Classified top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-top-banner1.png"
        ),
        ModuleTemplate(
            "Event just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-just-above-footer-text-only1.png"
        ),
        ModuleTemplate(
            "Event top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-top-banner1.png"
        ),
        ModuleTemplate(
            "Job LHS",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/job-LHS-of-page1.png"
        ),
        ModuleTemplate(
            "Classified above browsing history",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-above-browsing-history1.png"
        ),
        ModuleTemplate(
            "Classified just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/classified-just-above-footer-text-only.png"
        ),
        ModuleTemplate(
            "Classified just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/event-just-above-footer-image-only1.png"
        ),
    )
)

val dashboardLocationTemplate = ModuleWiseTemplate(
    listOf(
        ModuleTemplate(
            "Dashboard just above footer text only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-just-above-footer-text-only.png"
        ),
        ModuleTemplate(
            "Dashboard just above footer image only",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-just-above-footer-image-only.png"
        ),
        ModuleTemplate(
            "Dashboard top banner",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-top-banner.png"
        ),
        ModuleTemplate(
            "Dashboard below discover aaonri",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-below-discover-aaonri.png"
        ),
        ModuleTemplate(
            "Dashboard below interest area",
            "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-below-interest-area.png"
        ),
    )
)








