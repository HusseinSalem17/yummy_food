package com.hussein.mealz_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*
        System Design
            الوقتي لما يجيلي اي project او تاسك جديده فمحتاج اعملها SystemDesign حتي في الشركات الكبيره في Interview system Design دا يعني
            بشوف ال inputs الي جيالي كلها عشان اقدر اعمل outputs مناسبه لل inputs الي جيالي دي فلوقتي هعمل system Design للمشروع دا

            اول حاجه اتفقنا اننا نمشي ب architecture بتاعت uncle pop الي امسه onion architecture android فلو عملت بحث بنفس الاسم
            الي انا كتبته دا هلاقي في موقع medium مقاله بتكلم عنه كويس جدا وانه ازاي apply clean architecture جوا الاندرويد فيما معناه ان
            هعمل Three Layers (APP, DATA, DOMAIN) وكل حاجه مسؤلة عن حاجه معينه

            ال DOMAIN هيبقي abstract layer عن كل حاجه هتحصل فال Application ملهاش علاقه باي specific library in android

            ال DATA هي MID level layer between app & domain بتعمل handling للداتا ما بين ال App & Domain

            وال App بيبقي فيه ال ViewModel & Activity & Fragments & Dependency Injection

        Flow Chart
            تاني خطوة بعد ما عرفنا ال clean architecture الي همشي عليه فمحتاج اعرف ال flow بتاع ال App هيمشي ازاي فمحتاج اروح علي اي website
            بنرسم عليه واحد منهم اسمه Diagrams.net فاول حاجه فال Flow بتاعنا بتبقي ال View الي هي ال Activity OR Fragment الي هتبان لل user
            الي هي مسؤليتها كلها انا show data to user او get Action from user فهو الوقتي محاج حاجه توفرله الداتا دي جاهزه فالحاجه التانيه دي بقي
            اسمها لو انا همشي بال MVVM هيبقي اسمها ViewModel فكدا ال View هيكلم ال ViewModel عشان ال ViewModel يوفر لل View الداتا الي هو عايزها
            ال ViewModel وظيفته بقي انه handle OR format الداتا الي راجعه من ال Server OR offline Database فيظبطها لل View عشان
            ال View يجيب الداتا دي directly هحط فين بقي ال logic OR business Logic OR validation دا بقي هيتحط فحاجه اسمها useCase
            فدي بالنسبالي هي العقل االمدبر لل Application كله فهي بحط فيها ال business Logic الخاص ب Application بتاعي
            كدا الي View هيكلم ال ViewModel يقوله انا عايز الداتا دي فال viewModel وظيفته format data for View هيبجيب الداتا دي بقي من UseCase
            الي هو هيبقي فهيا ال business logic فبالتالي ال useCase ميعرفش حاجه عن Source of Data الي جياله عشان اعرف بقي ال source بتاع
            ال Data الي راجعه فمحتاج حاجه اسمها repo فمسؤلية ال repo انها تبقي عارفه الداتا الي جايه ال source بتاعها منين فكدا ال UseCase هحط فيها ال Logic
            بس هو ميعرفش source of Data عشان يعرف الداتا جايه منين هنخليه يكلم ال repo عشان يجيب الداتا دي فال repo مسؤليته بس انه يقرر الداتا راجعه منين
            فهو فعلا الداتا راجعه من حاجتين ياما offline Database عن طريق ال Dao ياما online Database فهيبقي عن طريق API Service بتاع retrofit
            فهو ال repo الي هيقرر الداتا تيجي منين هيبقي عنده الاتنين

            مختصر : الموقتي ال View هيقول لل ViewModel انه الوقتي load يعني بيعمل بروجرس بار مثلا فمحتاج شويه داتا فل ViewModel هيقول لل useCase
            انا محتاج شويه داتا معينه فل useCase هيعمل business logic ويكلم ال Repo وهي تقرر بقي تجيب الداتا offline from(Dao) OR online(retrofit)
            وبعدين الداتا ترجع لل repo وال repo يرجع لل useCase ويعمل عليها شويه business logic وبعدها من useCase ل ViewModel وال ViewModel يظبط
            الداتا دي ويرجع بقي الداتا لل View الداتا بس الي عايزها تظهر للمستخدم فال View تظهر الداتا دي

            هجيب الداتا بيقي من meals API واول ناتج فلبحث بالاسم دا هدخل عليه وهلاقي حاجه فلنص بقي اسمها List all meals Categories فدا الينك الي هجيب منه الداتا
            وهاخد اللينك واطبعه وهو هيبقي json فعشان اعرف اقراه هروح ل JSON formatter واخد كل الي ظهر واطبعه وبعدها هو هيظبط شكل ال json بقي فل json دا هو ال response
            الي هستخدمه فل app ومحتاج بقي اعمله module (convert json to module)
         */
    /*
        what is useCase ?
            الوقتي عندي شخص اشتري عربية فطبيعي هيروح للبنزينه عشان يمول العربيه فالبنزينه بيبقي فيها العداد الي بيمول منها العربيه ونتعق اكتر ورا العداد دا بيجيب البنزين دا من التانك
            بيكون تحت الارض والتانك دا اصلا البنزين الي فيه دا جاي منين ياما جاي من تحت الارض يعني البر ياما من البحر لو قارنا بقي المثال دا بال flow بتاعنا الي هو كان فيه viewModel
            وكدا هلاقي ان العربيه هي ال view والشخص دا بيجيب البنزين من البنزينه ويحطه فلعربيه فكدا الشخص يبقي ViewModel ودا كانه بيهندل الداتا الي جايه من مكان ما ويوديها لل View
            بلسنبالي بقي التانك هو عباره عن repository هو مستودع جواه بترول بتاع البنزينه كلها والبر هو عباره عن source 1 والبحر هباره عن source 2 كدا يبقي لسه ال useCase
            هي بقي العدادد وهو اسمه useCase علي مسمي لانه البنزينه بيبقي فيها اكتر من حاجه وحاجه من ضمن استخداامات البنزينه هي العداد دا الي هو اقدر بيه امول بنزين كدا تمام

            The Car depends on ViewModel and ViewModel depends on UseCase متعرقش مصدر البنرول منين فبالتالي هي
            depends on Repo عشان تجيب منه البترول
            انما ال Repo بقي هو الي بيقرر انهي مصدر الي يبقي ياخد منه ممكن يبقي ميكس من المصدرين عادي
            لو بصين كمان علبنزينه مش هيبقي فيها بس العدادات لا فيها كمان تغير زيت وكمان ال carWash فكدا بلنسبالي كل واحده فيهم UseCase منفصله اصلا فبالتالي كدا البنزينه عباره
            عن Application جواه كذا UseCase فلخدمات الي جوا البنزينه هي تغير زيت وعداد البنزين وال carWash عشان افارن دا ال application بتاعي بقول اي الخدمات
            ال application بتاعي بيعملها فلو بصبت App Meals بس بيجيب list ويعرضها في RecyclerView فل UseCase بتاعتي الاساسيه هي getMeals هي ال هتجيب
            ال Meals عشان اعدل فيها واعمل الي عاوزه فهي واحده من الخدمات الي بيقدمها ال Application بتاعي زي مالبنزينه فيها اكتر من خدمه
    */
    /*
        التنفيذ
            الوقتي بعد ما عملت البروجيكت فلو بصيت عليه هلاقيه متقسم ل one Module اسمه app وشويه settings خاصه بال Application بتاعي (Gradle Scripts)
            فهو كدا android studio عملي اصلا ال Module (app) انا الوقتي محتاج اعمل 2 Modules الي هما DATA & Domain فعشان اعملهم عملت كدا
            File -> new Module => Android Library   then made the name data , the same thing with domain
            كدا انا قسمت ال project ل 3 Modules (app, Data, module) كدا عندي ال modules الي اتكلمت عنهم في article for clean architect (onion architecture)

        Domain Layer
            الوقتي ال Domain الي بيتحط فيها هو ال Base Modules الي هستخدمها فال Application كله وكمان بيتحط فيها interface خاص بال repository عشان يبقي فيها Function Abstract
            جدا الي هملاها في ال dataLayer ففل domain بنحط بس Interfaces فاضيه خالص لل repo ونحط كمان ال UseCase الي بستخدمها عشان ال ViewModel عشان كدا
            عملت packages(repo & entity & useCase) الوقتي اول حاجه محتاج اعملها الي هو module للداتا الي راجعه من ال response السيرفر عشان module الداتا دي نزلت
            ال plugin دي Json to Kotlin classes فبعدها هروح اعمل new -> Kotlin data class File from JSON وطبعت كل الي في json وسميته CategoryResponse وخلي
            بالك اول حرف بيبقي كابتل هلقيه بقي هو عمل الملفات كلها المطلوبه لوحده دلوقتي تاني محتاج اعملها هي new interface عشان اخليه ال repo بتاعتنا فعملت MealsRepo وهحط فيه كل ال Functions
            الي هحتاجها من ال repo دي فعملت fun getMealsFromRemote ولسه هملاها في dataLayer
            الوقتي هعمل useCase ففل UseCase بتاعتي الاساسيه هي getMeals هي ال هتجيب ال Meals عشان اعدل فيها واعمل الي عاوزه فعملت class GetMealz ووزي ما قولت
            ال useCase depends on Repo فعايزها بقي فلكود تبقي معتمد ال useCase علي ال Repo فخليته يستقبل objectFrom MealsRepo as parameter in his constructor
            وكدا خليت ال UseCase depends on Repo زي مثال العداد بيعتمد علي التانك البنزين الي تحت الارض زي ما قولت فلشرح فدولقتي جوا كلاس GetMealz محتاج function واحده
            تبقي جواها المفروض تناديي علي ال getMealsFromRemote الي جوا MealsRepo عشان لو عندي كلاس واحد عايز اعمل فيه function واحده ففي حاجه سهله جدا اسمها operator fun وخليت اسمها invoke
            وعملت كدا عشان انا عندي كلاس جواه function واحده بدل ما انادي علي اسم ال function الي موجوده جوا ال class دا لا فانا خليتها operator fun بالتالي لو جيت فاي وقت ناديت علي
            الكلاس دا وقولتله GetMealz() من غير ما ابعت parameter فهيروح هو علطول ينادي علي ال function طلاما هي operator fun وخليتها بقي كمان suspend عشان هستخدم coroutines
            الوقتي انا كنت قايل ان ال useCase هي العقل المدبر لل App وهي هنا شايف مبتعملش حاجه هي بس بتنادي علي function الي موجوده جوا ال repo لي مشكلتش الكلام دا وخليته جوا ال ViewModel علطول
            بحيث انادي علي ال repo من ال ViewModel اسرع عشان تعرف السبب ابحث why do we need usecase android ? واختر موقع proandroiddev.com فيها article
            مختصر الي فيها انه اوقات كتير ببقي الي في ال useCase أني بنادي علي function جوا ال Repo بس فبلرغم من دا في اسباب كتيره لاستخدام ال useCase وهي اول حاجه لو ال usaCase بتعمل logic او مبتعملش خليها
            تاني حاجه لو في اضافات فلمستقبل علي الكود فدا بيسهل عليا التطويرات بيبقي التغير فهيا بس تالت حاجه وهي Screaming Architecture وهو لما تشوف ال App تعرف هو بيعمل اي عن طريق ال UseCase الي هو الخدمات الي فيه

        Data Layer
            ال DataLayer بقي هي الي بعمل فيها ال implementation بتاع ال interfaces الي عملتها في ال domain layer وبيبقي فيها ال API Service و Room DataBase Interfaces لو هستخدم Room بيبقي
            1. ال Implementation بتاع ال interfaces in Repo
            2. تاني حاجه محتاج يبقي معايا ال Api service بتاع retrofit عشان نبدا نعمله interfaces عشان نقدر نملها بال dependency injection
            3. محتاجين ال data Layer يكون عارف Reference علي ال Domain Layer
            فانا الوقتي هعمل اول حاجه اني اعمل reference علي ال Domain Layer لل Data Layer فهروح علي build.gradle(Mealz_App.data) وعملت كدا implementation project(path: ':domain')
            كدا هو خد reference to Domain layer واقدر access كل الداتا الي بداخل الي Domain Layer الوقتي عشان اعمل ال Api Service بتاعت retrofit فمحتاجين ال dependencies for retrofit & coroutines
            فحطتهم بعدها عملت package اسمها remote عشان هيبقي فيها الداتا ال remote و package repo عشان هيبقي فيها ال implementation بتاع الي interfaces in repo وعملت interface ApiService in remote
            وعملت فيه function واحده بس الي هي getMeals هترجع بس CategoryResponse ومش هتاخد حاجه وهي هتعرف ترجعلي CategoryResponse وهي في dataLayer والكلاس التاني في Domain layer
            ودا عشان انا عامل reference on DomainLayer from Data Layer فداله getMeals كدا فاضيه متعرفش حاجه retrofit فعشان اخليها تعرف عملت Get(after base Url)@ وكدا خصلت ال ApiService
            ناقصني بس اني اعمل dependencyInjection ودا هعمله في AppLayer حاجه لسه ناقصه في ال DataLayer هو اني اعمل Implementation بتاع ال repo عشان كدا عملت MealsRepoImpl(Impl -> implementation)
            عملت فيه package repo المه زي ما كنت قايل ان ال repo تعتمد علي حاجتين او حاجه واحده الي هما ApiService & Dao الوقتي هعمل بتاع ال online ApiService فالوقتي محتاج يبقي عندي reference on ApiService
            عشان استخدمها جوا ال Repo عشان كدا خليته ياخد parameter ApiService ويرث MealsRepo ودا عشان ينفذ ال methods الي فيه وخليت الداله تساوي apiService.getMeals عشان يجيب اليسته الي فلينك الي معمول

        App Layer
            بيبقي فيها كل حاجه ليها علاقه بال ui(view) وبيبقي فيها ال ViewModel او ممكن انقل ال ViewModel واحطه في Layer تانيه اسمها Presentation بس عادي معتعملتش وتالت حاجه انه بيتحط فيها ال DI فدا الي هشتغل فيه الوقتي
            قبل ما ابدا محتاج ارسم حاجه اسمها Directed acyclic graph (Dag) عشان كدا Dagger اسمها كدا عشان بتحول ارسم دا لكود ودا سبب اسمها وطلع حاجه جديده ليها اسمها DaggerHilt فدا الي هستخدمه الوقتي DaggerHilt
            قبل ما ابدا هرسم ال Dag زي ما وقفت اخر مره (last time (view -> viewModel -> useCase -> Repo -> (ApiService & Dao ال ApiService بيعتمد علي كذا حاجه بقي اول حاجه انه ياخد RetrofitObject
            وبعد ما ياخد RetrofitObject محتاج يعتمد علي OkHttpBuilder فل OkHttp متعتمدش علي حاجه دي اخر حاجه فيهم فلوقتي ييعني اي بقي dependency الي هو مثلا ال RetrofitObject depends on OkHttp
            فكدا ال OkHttp عباره عن dependency لل RetrofitObject فيعني اي DI يعني محتاح Third part Library to inject the dependency للحاجه الي انا محتاجها فيها يعني مثلا في ال ViewModel محتاج ال UseCase
            فانا مش كل مره محتاج فيها ال UseCase هعمل newInstance منها وابعته لل ViewModel فلا DaggerHilt هي الي هتعمل كدا عن طريق DI فوقتي انا محتاج كل ال dependencies بلترتيب زي ما فلرسم لغايه ال View
            فهمشي من تحت OkHttp واخلي DaggerHilt توفره ل retrofitObject و retrofitObject اوفرها ل ApiService وهتاخد OkHttp وال ApiService محتاج اوفرها لل Repo فل ApiService هتاخد RetrofitObject
            وال Repo محتاج اوفرها لل UseCase فخهليها جواها ال ApiService وكذلك لغايه ال View فانا كدا محتاج Three Modules الاولي هي UseCaseModule عشان توفرلي UseCase وتاني حاجه RepoModule عشان توفرلي ال Repo
            تالت حاجه NetworkModule ودي هتضم كل حاجه من التلات حاجات دول ApiService & RetrofitObject & OkHttp عشان توفرهم لل Repo في سؤوال الوقتي لي معملت Module لل ViewModel دا عشان Hilt بتوفرلي حاجه
            اسمها HiltViewModel هو بيعملها من غير ما اعمل Module السؤال التاني مش انا الي كاتب ال UseCas & Rep & ApiService لي عملتهم Modules مع ان ال Modules دي بنبقي محتاجينها اكتر لما يبقي عندي كلاس مش انا
            الي كاتبه فمقدرش اعملهم ConstructorInjection دا لانه للقراءه بس فبتالي محتاج اعمله Module زي RetrofitObject بس الكلام صح لو انا مش ماشي بال Layers لكن لو ماشي بال Layers فمش محتاج استخدم Hilt جوا
            ال Data Layer OR Domain Layer فعشان كدا محتاج اعملهم Module مختصره عشان اوفرهم في ال DI ال Modules مختلقه عن ال Modules بتاعت CleanArchitecture دي بس تبع Hilt عشان توفرهم هي زي Dagger
            عملت di(package) وضفت ال dependencies for hilt in gradle(app) وبعدها محتاج اخش علي gradle(app) محتاج اضيف ال plugin بتاع hilt ودا عشان اقدر استخدمها
            id 'com.google.dagger.hilt.android' version '2.41' apply false
            id 'kotlin-kapt'
            وعملت Object NetworkModule باقي الشرح فال Object
            المفروض بعد ما خلصت الوقتي الي بيعتمد علي ال ApiService هو ال Repo عشان كدا عملت RepoModule انا خلصت ال NetworkModule بس فعملت Object RepoModule باقي الشرح فيه
            فخلص ال repoModule نفس الكلام مع ال useCaseModule والشرح فيها وكدا مش محتاج اعمل حاجه تاني في DI عشان اال ViewModel هو الي هيستخدم ال useCase وال ViewModel اقدر اعمله Inject عن طريق hiltViewModel

            وكدا خلصت ال DI وعرفت ازاي اعمل DI for NetworkModule & RepoModule & UseCaseModule الوقتي دور ال ViewModel الاول هحط ال dependencies for ViewModel in gradle(app) عشان ابدا بقي اعمل
            ال ViewModel عملت class MealsViewModel باقي الشرح فيه

            كدا خلصت ال ViewModel الي عملته الوقتي ضفت permission Internet وكمان Hilt عشان تشتغل محتاجه Hilt application file OR Hilt application class واحط ال Application دا في Manifest
            عشان كدا عملت class MealzApplicaton وحطت فوقها @HiltAndroidApp وخليته يث Application دا زي ما بيحصل في Dagger وحطيت دا في android:name=".MealzApplication" ال Mainifests
            محتاج بردوا عشان اكمل hilt انه فوق ال activity اقول ان دي ال entryPoint بتاعت ال Application دا لان اي Activity OR Fragment in hilt محتاج Annotate الي Fragment او Activity
            بتاع البدايه اخر حاجه بقي انا كنت عامل invoke in GetMealz وعامله suspend فانا محتاج اي حاجه تنادي علي ال useCase والي بعده كمان يبقي suspend ودا لان كل ال hierarchy بتاعها لازم يبقي suspend
            عشان كدا عملت ال suspend getMealsFromRemote() in MealsRepoImpl & suspend getMeals() in ApiService كدا خليت كل حاجه suspend عشان اقدر انادي عليها ب coroutines

            الوقتي بقي هشتغل بال MVVM معناه ان هنادي علي ال ViewModel هقوله هات ال list من السيرفر و هستمع لل Mutable stateFlow الموجود في ال ViewModel لما يجيله اي داتا اقدر اسمعها واشوف اعمل بيها اي
            فاول حاجه محتاج انادي علي ال ViewModel عشان اعمل كدا جبت instance from ViewModel وبعدها استدعيت getMeals ودا حصل من غير ما ابعت دي parameter لانه كان هياخد كود كتير ودا فايدة ال hilt
            بعدها استدعيت collect from categories دي معناها اني هستمع لحاجات الي جوا ال category دي بس collect دي عباره عن suspend function فلمفروض تنادي من Coroutines
            والوقتي لما يجيله response محتاج اضيفه لل Adapter واضيف ال Adepter to RecyclerView

            خلي بالك عملت ? عشان ال CategoryResponse مخليه nullable عشان يقدر ياخد Null ك intial state فعملت العلامه عشان فمنيفزش اصلا وينادي ال categories لو هي ب null
     */

    //instance from viewModel
    private val viewModel: MealsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv: RecyclerView = findViewById(R.id.category_rv)
        //With DI
        /*
        .
        .
        .
        private val useCase=GetMealz()

        private val viewModelDum=MealsViewModel()
         */
        //with DI
        viewModel.getMeals()

        val mealsAdapter = MealsAdapter()
        lifecycleScope.launch {
            viewModel.categoriess.collect {
                mealsAdapter.submitList(it?.categories)
                rv.adapter = mealsAdapter
            }
        }

    }
}