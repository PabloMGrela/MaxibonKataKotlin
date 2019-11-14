package com.karumi.maxibonkata

import io.kotlintest.data.forall
import io.kotlintest.matchers.haveLength
import io.kotlintest.properties.assertAll
import io.kotlintest.properties.forAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class PropertyExample : StringSpec() {
    init {
        "String size" {
            assertAll { a: String, b: String ->
                (a + b) should haveLength(a.length + b.length)
            }
        }
    }
}

class StringSpecExample : StringSpec({
    "maximum of two numbers" {
        forall(
            row(1, 5, 5),
            row(1, 0, 1),
            row(0, 0, 0)
        ) { a, b, max ->
            a.coerceAtLeast(b) shouldBe max
        }
    }
})

class DevelopersTest : ShouldSpec() {
    init {
        "Strings"{
            should("any value plus empty string is equal to any value") {
                forAll { value: String ->
                    println(value)
                    value + "" == value
                }
            }
            should("two sum strings has same length") {
                forAll { value: String, value2: String ->
                    println(value)
                    value.length + value2.length == (value + value2).length
                }
            }
            should("two consecutive strings can be reversed twice") {
                forAll { value: String ->
                    println(value)
                    value == value.reversed().reversed()
                }
            }
        }
        "Developers" {
            should("always grab a positive number of maxibons") {
                forAll { numberOfMaxibons: Int ->
                    val dev = Developer(numberOfMaxibons = numberOfMaxibons)
                    dev.maxibonsToGrab >= 0
                }
            }
        }
    }
}