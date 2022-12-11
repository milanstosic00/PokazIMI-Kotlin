package com.example.pokazimi.ui.screens

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.pokazimi.R
import com.mapbox.search.ApiType
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.mapbox.search.offline.OfflineResponseInfo
import com.mapbox.search.offline.OfflineSearchEngine
import com.mapbox.search.offline.OfflineSearchEngineSettings
import com.mapbox.search.offline.OfflineSearchResult
import com.mapbox.search.record.HistoryRecord
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.ui.adapter.engines.SearchEngineUiAdapter
import com.mapbox.search.ui.view.CommonSearchViewConfiguration
import com.mapbox.search.ui.view.DistanceUnitType
import com.mapbox.search.ui.view.SearchResultsView

@Composable
fun SearchLocationScreen(navController: NavHostController) {
    val context = LocalContext.current

    SearchLocationHeader(navController)

    AndroidView(
        factory = { View.inflate(it, R.layout.search, null) },
        modifier = Modifier
            .fillMaxWidth()
            .absoluteOffset(y = 55.dp),
        update = {
            val accessToken = context.getString(R.string.mapbox_access_token)
            val queryEditText = it.findViewById<EditText>(R.id.query_edit_text)
            val searchResultsView = it.findViewById<SearchResultsView>(R.id.search_results_view)

            try {
                searchResultsView.initialize(
                    SearchResultsView.Configuration(
                        commonConfiguration = CommonSearchViewConfiguration(DistanceUnitType.METRIC)
                    )
                )
            }
            catch (e: Exception) {
                print("Error : ${e.message}")
            }

            val searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                apiType = ApiType.GEOCODING,
                settings = SearchEngineSettings(accessToken)
            )

            val offlineSearchEngine = OfflineSearchEngine.create(
                OfflineSearchEngineSettings(accessToken)
            )

            val searchEngineUiAdapter = SearchEngineUiAdapter(
                view = searchResultsView,
                searchEngine = searchEngine,
                offlineSearchEngine = offlineSearchEngine
            )

            searchEngineUiAdapter.addSearchListener(object : SearchEngineUiAdapter.SearchListener {

                private fun showToast(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun onSuggestionsShown(suggestions: List<SearchSuggestion>, responseInfo: ResponseInfo) {
                    // not implemented
                }

                override fun onCategoryResultsShown(
                    suggestion: SearchSuggestion,
                    results: List<SearchResult>,
                    responseInfo: ResponseInfo
                ) {
                    // not implemented
                }

                override fun onOfflineSearchResultsShown(results: List<OfflineSearchResult>, responseInfo: OfflineResponseInfo) {
                    // not implemented
                }

                override fun onSuggestionSelected(searchSuggestion: SearchSuggestion): Boolean {
                    return false
                }

                override fun onSearchResultSelected(searchResult: SearchResult, responseInfo: ResponseInfo) {
                    val lon = searchResult.coordinate.coordinates()[0]
                    val lat = searchResult.coordinate.coordinates()[1]
                    navController.navigate("map/false/false/desc/$lon/$lat")
                }

                override fun onOfflineSearchResultSelected(searchResult: OfflineSearchResult, responseInfo: OfflineResponseInfo) {

                    val lon = searchResult.coordinate.coordinates()[0]
                    val lat = searchResult.coordinate.coordinates()[1]
                    navController.navigate("map/false/false/desc/$lon/$lat")
                }

                override fun onError(e: Exception) {
                    showToast("Error happened: $e")
                    println("Error --- $e")
                }

                override fun onHistoryItemClick(historyRecord: HistoryRecord) {

                    val lon = historyRecord.coordinate.coordinates()[0]
                    val lat = historyRecord.coordinate.coordinates()[1]
                    navController.navigate("map/false/false/desc/$lon/$lat")
                }

                override fun onPopulateQueryClick(suggestion: SearchSuggestion, responseInfo: ResponseInfo) {
                    queryEditText.setText(suggestion.name)
                }

                override fun onFeedbackItemClick(responseInfo: ResponseInfo) {
                    // not implemented
                }
            })

            queryEditText.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
                    //searchResultsView
                    searchEngineUiAdapter.search(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // not implemented
                }

                override fun afterTextChanged(e: Editable) { /* not implemented */ }
            })
        }
    )
}

@Composable
fun SearchLocationHeader(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    Modifier.size(30.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Search Location",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.absoluteOffset(y = (-2).dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {

        }
    }
}
