# Android App

## Notes from learning Android development

"The onCreate() function is the entry point to this Android app and calls other functions to build the user interface. In Kotlin programs, the main() function is the entry point/starting point of execution. In Android apps, the onCreate() function fills that role."

"An activity is a single, focused thing that the user can do. Almost all activities interact with the user, so the Activity class takes care of creating a window for you in which you can place your UI with setContentView(View)."

"The setContent() function within the onCreate() function is used to define your layout through composable functions. All functions marked with the @Composable annotation can be called from the setContent() function or from other Composable functions. The annotation tells the Kotlin compiler that this function is used by Jetpack Compose to generate the UI."

"A Surface is a container that represents a section of UI where you can alter the appearance, such as the background color or border. The Surface function is used to create a Surface composable that can be used to define the appearance of the UI."

"A Modifier is used to augment or decorate a composable. One modifier you can use is the padding modifier, which adds space around the element (in this case, adding space around the text). This is accomplished by using the Modifier.padding() function."

"To display a scrollable column we use a LazyColumn. LazyColumn renders only the visible items on screen, allowing performance gains when rendering a big list."