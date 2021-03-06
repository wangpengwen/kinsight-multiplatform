package com.kinsight.kinsightmultiplatform.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kinsight.kinsightmultiplatform.Constans.PICK_TICKER_REQUEST
import com.kinsight.kinsightmultiplatform.R
import com.kinsight.kinsightmultiplatform.ViewModels.IdeaCreateViewModel
import com.kinsight.kinsightmultiplatform.extensions.getViewModel
import com.kinsight.kinsightmultiplatform.models.IdeaModel
import kotlinx.android.synthetic.main.activity_idea_create.*
import android.net.Uri
import android.widget.RadioButton
import androidx.lifecycle.Observer
import com.kinsight.kinsightmultiplatform.models.TickerPriceModel
import com.kinsight.kinsightmultiplatform.resources.Strings


class IdeaCreateActivity : FullScreenActivity(),
    PickIdeaDurationFragment.OnFragmentInteractionListener {


    inner class IdeaCreateUtilityClass {
        val targetPriceValue : Double
        get() = targetPrice.text.toString().plus(".00").toDouble()

        val stopLossValue: Int
        get() = stopLoss.text.toString().toInt()

        val benchMarkTicker: String = "SPX"

        val initialAlphaValue: Double = 0.0

        val ideaReason: String = "Target Price"
        val stockCurrency: String = "USD"
    }

    private var companyName: String? = null
    private var companyTicker: String? = null
    private var companyPrice: Double? = null
    private var companyDirection: String? = null
    private var companyDirectionId: Int? = null
    private var timeHorizon: String? = null
    private var companyConvictionId: Int? = null

    private val viewModel by lazy { getViewModel {IdeaCreateViewModel()}}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idea_create2)

        val startIntent = intent
        val nextId = startIntent.getIntExtra("nextId", 0)


        chooseTicker.setOnClickListener {
            val intent = Intent(this, TickerSearchActivity::class.java)
            startActivityForResult(intent, PICK_TICKER_REQUEST)
        }


        initViewModelListener()

        saveIdea.alpha = 0.3f


        saveIdea.setOnClickListener {

            if (companyTicker == null) {
                showUserWarning("Please choose ticker first!")
            }
            else if (companyDirection == null || timeHorizon == null || companyConvictionId == null) {
               showUserWarning("Direction, Conviction and Investment Horizon are required!")
            }
            else {
                try {
                    val ideaCreateUtilityClass = IdeaCreateUtilityClass()
                   // val targetPrice = ideaCreateUtilityClass.targetPriceValue
                   // val stopLoss = ideaCreateUtilityClass.stopLossValue
                    val newIdea = IdeaModel(
                        id = nextId,
                        alpha = ideaCreateUtilityClass.initialAlphaValue,
                        benchMarkTicker = ideaCreateUtilityClass.benchMarkTicker,
                        //benchMarkTickerDesk= "SP 500 Index",
                        benchMarkCurrentPrice = 2856.66,
                        benchMarkPerformance = 0.392,
                        convictionId = companyConvictionId!!,
                        currentPrice = companyPrice!!,
                        direction = companyDirection!!,
                        directionId = if (companyDirection!! == "Long")  1 else 2,
                        entryPrice = companyPrice!!,
                        reason = ideaCreateUtilityClass.ideaReason,
                        securityName = companyName ?: companyTicker!!,
                        securityTicker = companyTicker!!,
                        stockCurrency = ideaCreateUtilityClass.stockCurrency,
                        stopLoss = ideaCreateUtilityClass.stopLossValue,
                        stopLossValue = 313.4823,
                        targetPrice = ideaCreateUtilityClass.targetPriceValue,
                        targetPricePercentage = 0.0,
                        timeHorizon = timeHorizon!!,
                        createdBy = "Dmitri",
                        createdFrom = "Android",
                        previousCurrentPrice = companyPrice!!,
                        isActive = true

                    )

                    viewModel.saveIdea(newIdea)
                    finish()
                } catch (e: Throwable) {
                    showUserWarning("Failed to save: ${e.message}")
                    Log.e("IDEA_", e.message + e.stackTrace)
                }
            }
        }
    }

    private fun initViewModelListener() {
        viewModel.getTickerPrice().observe (
            this,
            Observer<TickerPriceModel> { tickerPriceModel ->
                Log.i("APP", "Ticker price model observed: $tickerPriceModel")
                val ticker = companyTicker
                companyPrice = tickerPriceModel.latestPrice
                chooseTicker.text = "$ticker  |  Price: USD ${companyPrice} | Benchmark: SPX"

            }
        )
    }

    fun onRadioButtonClicked(v: View){
            when ((v as RadioButton).text.toString()){
                Strings.direction_long ->  companyDirection = Strings.direction_long
                Strings.direction_short -> companyDirection = Strings.direction_short
                Strings.conviction_low -> companyConvictionId = 3
                Strings.conviction_medium -> companyConvictionId = 2
                Strings.conviction_high -> companyConvictionId = 1
                Strings.time_horizon_one_month -> timeHorizon = Strings.time_horizon_one_month
                Strings.time_horizon_three_months -> timeHorizon = Strings.time_horizon_three_months
                Strings.time_horizon_one_week -> timeHorizon = Strings.time_horizon_one_week
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == PICK_TICKER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                companyTicker = data?.getStringExtra("ticker")

                chooseTicker.text = companyTicker
                companyName = data?.getStringExtra("companyName")
                viewModel.fetchTickerPrice(companyTicker!!)
             }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showEditDialog() {
      val fm = supportFragmentManager
      val fragment = PickIdeaDurationFragment.newInstance("Pick Idea Duration", "s");

        fragment.show(fm, "fragment_pick_idea_duration");
  }

    override fun onFragmentInteraction(uri: Uri) {
       println("callback from dialog fragment")
    }

}
