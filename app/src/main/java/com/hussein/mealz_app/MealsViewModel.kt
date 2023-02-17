package com.hussein.mealz_app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussein.domain.entity.CategoryResponse
import com.hussein.domain.usecase.GetMealz
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/*
    الاول محتاج اقوله ان الكلاس دا هيبقي عباره عن hilt viewModel عشان hilt يقدر يعمله DI جوا ال View بتاعنا او in activity عشان كدا عملت
    دا @HiltViewModel تاني حاجه محتاج اعملها ان الكلاس دا يبقي constructor عشان اقدر اباصي جواه ال useCase الي هستخدمها جوا ال ViewModel
    قالي هستخدمها useCase (getMealz) فشعان اقدر استخدمها هنا عمتل object of getMealz(as constructor parameter) وهو كدا اصلا
    انا عامل في ال Di انه لما يحتاج object from GetMealz هو هيجيبه لوحده من داله provideUseCase فلي هيبعت هو ال DI مش انا بعدا خليت الكلاس
    يرث ال ViewModel بعدها حطيت @inject عشان يعرف انه ال DI هو الي يجيب ال parameter وحطيت constructor عشان يعرف انه injectConstructor
    قبل ما تطلع hilt ايام Dagger بس كان بيبقي اصعب ان اعمل ViewModelInjection بس hilt سهلته عشان HiltViewModel & Inject constructor
    الوقتي جوا الViewModel هعمل function واجده ترجعلي ال Meals من ال useCase فعملت كدا getMealsUseCase() هو كدا هيفهم اني محتاج ال Meals لاني عامل invoke in GetMealz
    بس لازم اندها in coroutines عشان هي suspend function وحطيتها في try & catch عشان لوحصل error بس الوقتي ملا انادي علي getMealsUseCase() وترجعلي ال list Categorise
    مش هعرف ال Meals دي احطها فين فانا محتاج اخلي ال ViewModel يبقي stateHolder ومينفعش اخلي ال function ترجعلي ال category فدا مش كويس لان لما انادي عليها هترجعلي category
    بس مفيش حاجه هتشيل ال category دا عشان بقي اخلي في stateHolder يعني حاجه تشيل ال response الي هيرجع فمحتاج يبقي في property(parameter) in MealsViewModel
    وال porperty دي تبقي MutableStateFlow عشان يشيلي ال State بتاعت ال result السبب الرئيسي اني مش مخلي الداله ترجع ال category فهو لانها هترجعلي الناتج مره واحده بس
    ومش هقدر اعرف ال result تاني

    خليت الاولي MutableStateFlow عشان اقدر اعدل عليها في اي وقت لكن التانيه StateFlow بس عشان محتاجها تبقي الداتا الاخيره الي راجعه لل View فمحتاجها تبقي ثابته متتغيرش وكدا ميقدرش حد يغير
    من القيمه بتاعتها حتي لو حصل هاكينج
* */

@HiltViewModel
class MealsViewModel @Inject constructor(private val getMealsUseCase: GetMealz) : ViewModel() {

    //_ (underScore) for encapsulation
    private val _categories: MutableStateFlow<CategoryResponse?> = MutableStateFlow(null)

    //I made it StateFlow because it will not change again
    val categoriess: StateFlow<CategoryResponse?> = _categories
    fun getMeals() {
        viewModelScope.launch {
            try {
                _categories.value = getMealsUseCase()
            } catch (e: Exception) {
                Log.e("MealsViewModel", e.message.toString())
            }
        }
    }
}