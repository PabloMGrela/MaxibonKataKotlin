package com.karumi.maxibonkata

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class KarumiHQsTest : ShouldSpec() {

    init {
        "KarumiOffice"{
            should("start the day with 10 maxibons") {
                val office = KarumiHQs()

                office.maxibonsLeft shouldBe 10
            }

            should("always has more than two maxibons in the fridge") {
                forAll(Generators.AnyDeveloperGenerator()) { developer ->
                    val office = KarumiHQs()

                    office.openFridge(developer)

                    office.maxibonsLeft > 2
                }
            }

            should("buy 10 more maxibons if there are less than 3 in the fridge") {
                forAll(Generators.HungryDeveloperGenerator()) { developer ->
                    val office = KarumiHQs()
                    val initialMaxibons = office.maxibonsLeft

                    office.openFridge(developer)

                    val expectedMaxibons = calculateMaxibonsLeft(initialMaxibons, developer)
                    office.maxibonsLeft == expectedMaxibons
                }
            }

            should("request 10 more maxibons using the chat if there are less than 3 in the fridge") {
                forAll(Generators.HungryDeveloperGenerator()) { developer ->
                    val chat = MockChat()
                    val office = KarumiHQs(chat)

                    office.openFridge(developer)

                    chat.messageSent == "Hi guys, I'm ${developer.name}. We need more maxibons!"
                }
            }

            should("never request more maxibons to the team using the chat if there are more than 2 in the fridge") {
                forAll(Generators.NotSoHungryDeveloperGenerator()) { developer ->
                    val chat = MockChat()
                    val office = KarumiHQs(chat)
                    office.openFridge(developer)

                    chat.messageSent == null
                }
            }

            should("always has more than two maxibons in the fridge even if some karumies grab maxibons in group") {
                forAll(Gen.list(Generators.AnyDeveloperGenerator())) { developers ->
                    val office = KarumiHQs()

                    office.openFridge(developers)

                    office.maxibonsLeft > 2
                }
            }

            should("buy 10 more maxibons if there are less than 2 in the fridge when grabbing maxibons in group") {
                forAll(Gen.list(Generators.AnyDeveloperGenerator())) { developers ->
                    val office = KarumiHQs()
                    val initialMaxibons = office.maxibonsLeft

                    office.openFridge(developers)

                    val expectedMaxibons = calculateMaxibonsLeft(initialMaxibons, developers)
                    office.maxibonsLeft == expectedMaxibons
                }
            }

            should("request 10 more maxibons using the chat if there are less than 3 in the fridge when grabbing maxibons in group") {
                forAll(Gen.list(Generators.HungryDeveloperGenerator()).filter { it.isNotEmpty() }) { developers ->
                    val chat = MockChat()
                    val office = KarumiHQs(chat)

                    office.openFridge(developers)

                    chat.messageSent?.contains("We need more maxibons!") ?: false
                }
            }
        }
    }

    private fun calculateMaxibonsLeft(initialMaxibons: Int, dev: Developer): Int =
        calculateMaxibonsLeft(initialMaxibons, listOf(dev))

    private fun calculateMaxibonsLeft(initialMaxibons: Int, developers: List<Developer>): Int =
        developers.fold(initialMaxibons) { acc, dev ->
            var maxibonsLeft = Math.max(0, acc - dev.maxibonsToGrab)
            if (maxibonsLeft <= 2) {
                maxibonsLeft += 10
            }
            maxibonsLeft
        }

}