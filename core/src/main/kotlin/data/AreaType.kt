package data

sealed class AreaType {
    object MultiType : AreaType()
    object Residential: AreaType()
    object Commercial: AreaType()
}