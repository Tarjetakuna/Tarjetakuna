package com.github.sdp.tarjetakuna.ui

@RunWith(AndroidJUnit4::class)
class FilterCardsFragmentTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private val card1 = MagicCard(
        "Angel of Mercy", "Flying",
        MagicLayout.Normal, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    )

    private val card2 = MagicCard(
        "Meandering Towershell", "Islandwalk",
        MagicLayout.DoubleFaced, 5, "{3}{G}{G}",
        MagicSet("MT15", "Magic 2015"), 141,
        "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"
    )

    private val arrayCard = arrayListOf(card1, card2)

    private val layout = "Layout"
    private val normal = "Normal"
    private val token = "Token"
    private val phenomenon = "Phenomenon"
    private val leveler = "Leveler"
    private val angelOfMercy = "Angel of Mercy"
    private val angelOfSerenity = "Angel of Serenity"
    private val doubleFaced = "DoubleFaced"

    private val withLayoutText = withText(layout)
    private val withNormalText = withText(normal)
    private val withPhenomenonText = withText(phenomenon)
    private val withLevelerText = withText(leveler)
    private val withAngelOfMercyText = withText(angelOfMercy)
    private val withAngelOfSerenityText = withText(angelOfSerenity)
    private val withDoubleFacedText = withText(doubleFaced)
    private val withTokenText = withText(token)

    private val withIdLayoutTextView = withId(R.id.layoutTextView)
    private val withIdButton1 = withId(android.R.id.button1)
    private val withIdButton3 = withId(android.R.id.button3)
    private val withIdCmcTextView = withId(R.id.cmcTextView)
    private val withIdFilterButton = withId(R.id.filterButton)
    private val withIdCardNameTextView = withId(R.id.cardNameTextView)
    private val withIdReturnMainButton = withId(R.id.returnMainButton)

    @Before
    fun setUp() {
        val gson = Gson()
        val arrayCardJson = gson.toJson(arrayCard)

        // Create a bundle and set the parameters
        val bundle = Bundle()
        bundle.putString("cards", arrayCardJson)

        // Use the ActivityScenarioRule to launch the fragment
        activityRule = ActivityScenario.launch(MainActivity::class.java)

        // Get a reference to the fragment's view
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_filter, bundle)
        }
    }

    @Test
    fun testContainsTextView() {
        onView(withIdLayoutTextView).check(matches(isDisplayed()))
    }


    @Test
    fun testClickOnLayoutShowsLayoutList() {
        onView(withIdLayoutTextView).perform(click())
        onView(withLayoutText).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun testCanClickOnChoices() {
        onView(withIdLayoutTextView).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withPhenomenonText).inRoot(isDialog()).perform(click())
        onView(withTokenText).inRoot(isDialog()).perform(click())
        //button 1 = OK, button 3 = Cancel
        onView(withIdButton1).perform(click())
        onView(withIdLayoutTextView).check(matches(withText("$normal, $token, $phenomenon")))
    }

    @Test
    fun testValuesAllCleared() {
        onView(withIdLayoutTextView).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withPhenomenonText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdLayoutTextView).check(matches(withText("$normal, $phenomenon")))

        onView(withIdLayoutTextView).perform(click())
        onView(withIdButton3).perform(click())
        onView(withIdLayoutTextView).check(matches(withText("")))
    }

    @Test
    fun testCheckBoxUnchecked() {
        onView(withIdLayoutTextView).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withLevelerText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdLayoutTextView).check(matches(withLevelerText))
    }

    @Test
    fun testCheckBoxStillCheckedAfterConfirmingAndReopening() {
        onView(withIdLayoutTextView).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withLevelerText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdLayoutTextView).check(matches(withText("$normal, $leveler")))

        onView(withIdLayoutTextView).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdLayoutTextView).check(matches(withLevelerText))
    }

    @Test
    fun noChoiceSelectedMultipleChoiceWorks() {
        onView(withIdLayoutTextView).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdLayoutTextView).check(matches(withText("")))
    }

    @Test
    fun testSingleChoiceWorks() {
        onView(withIdCardNameTextView).perform(click())
        onView(withAngelOfMercyText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdCardNameTextView).check(matches(withAngelOfMercyText))
    }

    @Test
    fun testNoSelectedChoiceWorks() {
        onView(withIdCardNameTextView).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdCardNameTextView).check(matches(withText("")))
    }

    @Test
    fun testOnlySingleChoicePossible() {
        onView(withIdCardNameTextView).perform(click())
        onView(withAngelOfMercyText).inRoot(isDialog()).perform(click())
        onView(withAngelOfSerenityText).inRoot(isDialog()).perform(click())

        onView(withIdButton1).perform(click())
        onView(withIdCardNameTextView).check(matches(withAngelOfSerenityText))
    }

    @Test
    fun testSingleChoiceCleared() {
        onView(withIdCardNameTextView).perform(click())
        onView(withAngelOfMercyText).inRoot(isDialog()).perform(click())
        onView(withIdButton3).perform(click())
        onView(withIdCardNameTextView).check(matches(withText("")))
    }

    @Test
    fun testSingleChoiceStays() {
        onView(withIdCardNameTextView).perform(click())
        onView(withAngelOfMercyText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdCardNameTextView).check(matches(withAngelOfMercyText))

        onView(withIdCardNameTextView).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdCardNameTextView).check(matches(withAngelOfMercyText))
    }

    @Test
    fun testFilterButtonWithEmptyFilterWorks() {
        onView(withIdFilterButton).perform(click())
        onView(withText(arrayCard.toString())).inRoot(isDialog())
            .check(matches(withText(arrayCard.toString())))

    }

    @Test
    fun testFilterButtonWithEmptyName() {
        onView(withIdLayoutTextView).perform(click())
        onView(withDoubleFacedText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdFilterButton).perform(click())
        onView(withText("[$card2]")).inRoot(isDialog()).check(matches(withText("[$card2]")))

    }

    @Test
    fun testFilterButtonWorks() {
        onView(withIdCardNameTextView).perform(click())
        onView(withAngelOfMercyText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdLayoutTextView).perform(click())
        onView(withNormalText).inRoot(isDialog()).perform(click())
        onView(withLevelerText).inRoot(isDialog()).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdFilterButton).perform(click())
        onView(withText("[$card1]")).inRoot(isDialog()).check(matches(withText("[$card1]")))
    }

    @Test
    fun testFilterOnlyWithCMC() {
        onView(withIdCmcTextView).perform(click())
        onView(withText("5")).perform(click())
        onView(withText("7")).perform(click())
        onView(withIdButton1).perform(click())

        onView(withIdFilterButton).perform(click())
        onView(withText(arrayCard.toString())).inRoot(isDialog())
            .check(matches(withText(arrayCard.toString())))
    }

    @Test
    fun testFilterButtonIsEnabledAfterDialogClosed() {
        onView(withIdFilterButton).perform(click())
        onView(withIdButton1).perform(click())
        onView(withIdFilterButton).check((matches(isClickable())))
    }

    @Test
    fun testBackButtonWorks() {

        // Navigate back to the previous destination
        onView(withIdReturnMainButton).perform(click())

        // Verify that we've navigated back to the previous destination
        activityRule.onActivity { activity ->
            val navController = findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertThat(navController.currentDestination?.id, equalTo(R.id.nav_home))
        }

    }
}
