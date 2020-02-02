package com.dicoding.picodicloma.myunitanduitesting

class CuboidModel {
    private var width: Double = 0.0
    private var length: Double = 0.0
    private var height: Double = 0.0

    fun getVolume(): Double = this.width * this.length * this.height

    fun getSurfaceArea(): Double {
        return 2 * ((this.width * this.length) + (this.width * this.height) + (this.length * this.height))
    }

    fun getCircumference(): Double = 4 * (this.width + this.length + this.height)

    fun save(width: Double, length: Double, height: Double) {
        this.width = width
        this.length = length
        this.height = height
    }
}