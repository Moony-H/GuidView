# GuidView

뷰의 모습을 Bitmap으로 변환한 후 Canvas를 사용하여 화면에 직접 그리기 때문에

어떠한 뷰도 표한할 수 있는 뷰 입니다.

<br/>

<br/>

## 결과:

<br/>

![ezgif com-video-to-gif (3)](https://user-images.githubusercontent.com/53536205/230626220-67bc72a3-fbc0-4bc6-a2a6-cc42d54568ce.gif)

<br/>

<br/>

## 사용법

<br/>

GuideView는 미리 준비된 뷰 들을 사용하지 않습니다.

소개할 뷰 들을 넣어 지정할 수도 있고,

그 뷰를 소개하는 뷰 또한 직접 만들어서 넣을 수 있습니다.

위의 예시에서도 임시로 제가 만든 뷰를 넣었을 뿐, 실제 작업에서는 직접 DescriptionView를 작성해야 합니다.


<br/>

### 1. 다른 뷰를 등록해서 사용

먼저 사용힐 레이아웃의 마지막에 아래와 같이 추가합니다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    >

    <View
        android:id="@+id/test1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_margin="30dp"
        android:background="@color/purple_200"
        />

    <TextView
        android:textColor="@color/blue"
        android:id="@+id/test2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        android:textStyle="bold"
        android:textSize="20dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
        
        
    <!--many views-->


    <com.moonyh.guideview.GuideView
        android:id="@+id/guide_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />



</androidx.constraintlayout.widget.ConstraintLayout>
```

그 다음 설명을 할 뷰들을 세팅합니다.

```kotlin
binding.guideView.setTargetViews(
    arrayOf(
        binding.test1,
        binding.test2,
        binding.test3,
        binding.test4,
        binding.icon
    )
)
```

그리고 설명 시에 넣을 DescriptionView를 Builder Pattern으로 직접 만들어 세팅합니다.

<br/>

<br/>

### 옵션

<br/>

**setDeescriptionPosition:** 소개할 뷰의 상단, 하단, 오른쪽, 왼쪽 어느 곳에 DescriptionView를 넣을 지 결정하는 메서드 입니다.

<br/>

**setViewMargin:** 위의 setDescriptionPosition에서 결정한 위치에서, 소개할 뷰와의 거리를 결정할 수 있는 메서드 입니다.

<br/>

**setViewAnchor:** 소개할 뷰의 가운데와 설명할 뷰의 가운데를 기준으로, 앵커를 지정합니다. 기본 값은 0.5f 입니다.

<br/>

<br/>

### ex

만약 아래와 같이 세팅한다면 설명 뷰는 대상 뷰의 위에 위치하며, 50정도로 떨어져 있고, 설명 뷰의 0.1 지점이 대상 뷰의 0.5지점에 위치하게 됩니다.

<br/>
```kotlin
val firstDescriptionView=GuideDescriptionView.Builder(someCustomView)
    .setDescriptionPosition(DescriptionPosition.POSITION_TOP)
    .setViewMargin(50.0f)
    .setViewAnchor(0.1f)
    .build()
```

**결과**

<br/>

<img width="278" alt="스크린샷 2023-04-08 오전 3 24 00" src="https://user-images.githubusercontent.com/53536205/230658864-f87cea3f-4dd9-447f-a5b9-88d632eba959.png">

<br/>

이제 만든 DescriptionView를 setDescriptionViews() 메서드로 세팅합니다.

<br/>

<br/>

```kotlin
val descriptionViews= arrayOf(
    firstDescription,
    secondDescription,
    thirdDescription,
    forthDescription,
    fifthDescription
)

binding.guideView.setDescriptionViews(descriptionViews)


```

<br/>

이제 사용자가 원하는 시기에 GuideView.next()를 호출하면 다음 뷰를 설명하게 됩니다.



<br/>

```kotlin

someView.setOnClickListener { binding.guideView.next() }

```

<br/>

<br/>

마지막으로 모든 뷰를 설명한 후 next를 호출하면 GuideView 스스로 부모 뷰에서 삭제 됩니다.

<br/>

<br/>




### 2. 설명할 뷰 들의 부모 뷰로 사용

GuideView는 ConstraintLayout을 상속 받아 만든 ViewGroup 입니다.

따라서 아래와 같이 설명할 뷰의 parent로 설정하여도 무방합니다.

```xml
<?xml version="1.0" encoding="utf-8"?>

<!--레이아웃 최 상단에 배치-->

<com.moonyh.guideview.GuideView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/guide_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
>
    <!--constraintLayout 처럼 사용할 수 있다.-->

    <View
        android:id="@+id/test1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_margin="30dp"
        android:background="@color/purple_200"
        />

    <TextView
        android:textColor="@color/blue"
        android:id="@+id/test2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        android:textStyle="bold"
        android:textSize="20dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />



    <ImageView
        android:id="@+id/icon"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/ic_launcher_foreground"
        />



</com.moonyh.guideview.GuideView>
```


위와 같이 자식을 갖는 GuideView는 자동으로 자신의 자식 뷰 들을 세팅하게 됩니다.

따라서 위의 예시처럼 setTargetViews()를 호출하지 않아도 됩니다.

<br/>

<br/>

나머지 과정은 동일합니다.

DescriptionView를 Builder Pattern으로 직접 만들어 세팅합니다.

<br/>

<br/>

### 옵션

<br/>

**setDeescriptionPosition:** 소개할 뷰의 상단, 하단, 오른쪽, 왼쪽 어느 곳에 DescriptionView를 넣을 지 결정하는 메서드 입니다.

<br/>

**setViewMargin:** 위의 setDescriptionPosition에서 결정한 위치에서, 소개할 뷰와의 거리를 결정할 수 있는 메서드 입니다.

<br/>

**setViewAnchor:** 소개할 뷰의 가운데와 설명할 뷰의 가운데를 기준으로, 앵커를 지정합니다. 기본 값은 0.5f 입니다.

<br/>

<br/>

위와 다른 점은 마지막 까지 next를 호출하더라도 GuideView는 지워지지 않습니다.



