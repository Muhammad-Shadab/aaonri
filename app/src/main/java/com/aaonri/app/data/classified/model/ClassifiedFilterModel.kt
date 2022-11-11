package com.aaonri.app.data.classified.model

class ClassifiedFilterModel(
    var selectedCategory: String,
    var selectedSubCategory: String,
    var minPriceRange: String,
    var maxPriceRange: String,
    var zipCode: String,
    var zipCodeCheckBox: Boolean,
    var isDatePublishedSelected: Boolean,
    var isPriceLowToHighSelected: Boolean,
    var isPriceHighToLowSelected: Boolean,
)