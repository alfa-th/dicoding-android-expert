package com.dicoding.picodicloma.myunitanduitesting

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.*

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var cuboidModel: CuboidModel

    private val dummyL = 12.0
    private val dummyW = 7.0
    private val dummyH = 6.0

    private val dummyVol = 504.0
    private val dummyCir = 100.0
    private val dummySur = 396.0

    @Before
    fun before() {
        cuboidModel = mock(CuboidModel::class.java)
        mainViewModel = MainViewModel(cuboidModel)
    }

    @Test
    fun testVolume() {
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyL, dummyW, dummyH)

        val volume = mainViewModel.getVolume()

        assertEquals(dummyVol, volume, 0.0001)
    }

    @Test
    fun testCircumference() {
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyL, dummyW, dummyH)

        val circumference = mainViewModel.getCircumference()

        assertEquals(dummyCir, circumference, 0.0001)
    }

    @Test
    fun testSurfaceArea() {
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyL, dummyW, dummyH)

        val surfaceArea = mainViewModel.getSurfaceArea()

        assertEquals(dummySur, surfaceArea, 0.0001)
    }

    @Test
    fun testMockVolume() {
        `when`(mainViewModel.getVolume()).thenReturn(dummyVol)
        val volume = mainViewModel.getVolume()
        verify(cuboidModel).getVolume()
        assertEquals(dummyVol, volume, 0.0001)
    }
    @Test
    fun testMockCircumference() {
        `when`(mainViewModel.getCircumference()).thenReturn(dummyCir)
        val circumference = mainViewModel.getCircumference()
        verify(cuboidModel).getCircumference()
        assertEquals(dummyCir, circumference, 0.0001)
    }
    @Test
    fun testMockSurfaceArea() {
        `when`(mainViewModel.getSurfaceArea()).thenReturn(dummySur)
        val surfaceArea = mainViewModel.getSurfaceArea()
        verify(cuboidModel).getSurfaceArea()
        assertEquals(dummySur, surfaceArea, 0.0001)
    }

    @Test
    fun getCircumference() {
    }

    @Test
    fun getSurfaceArea() {
    }

    @Test
    fun getVolume() {
    }

    @Test
    fun save() {
    }
}