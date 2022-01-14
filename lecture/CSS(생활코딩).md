# CSS (생활코딩)

#### 디자인을 적용하는 방식

1. Inline: html 태그를 이용하는 방식
	```html
	<h1 style="color:red">Hello, World!</h1>
	```
	html 문법으로 작성된 style 태그를 사용하여 CSS(color:red) 삽입
2. CSS만의 방식
	```html
	<style>
		h2{color:blue}
	</style>
	```

### 선택자
#### 선택자와 선언
```html
	<head>
		<style>
			li{
				color:red;
				text-decoration:underline;
			}
		</style>
	</head>
```
- 두 개 이상의 속성을 적용할 경우 세미콜론으로 구별한다.
- h1(Selector) {color:(property) :(Value-Property Separator) red(value) ;(Declaration Separator)} (Declaration)
- 선택자, 속성, 속성값, 구분자

#### 선택자의 종류
- 추가적인 공부는 CSS Cheat Sheet 찾아서 살펴본다.
1. 태그 선택자
	```html
	<head>
		<style>
			li{
			...
			}
		</style>
	</head>
	```
	- `<li>` 태그 전체에 적용된다.
2. ID 선택자
	```html
	<head>
		<style>
			#select{
			...
			}
		</style>
	</head>
	<body>
		<ul>
			<li>CSS가 적용되지 않은 부분</li>
			<li id="select">CSS가 적용된 부분</li>
		</ul>
	</body>
	```
	- id 값에 따라서 적용 여부를 구별
	- 속성 id와 #select는 html의 문법
3. 클래스 선택자
	```html
	...
	<style>
		.deactive {
		}
	</style>
	...
	<h1 class="deactive">CSS적용된 제목 글1<//h1>
	...
	<li>CSS 미적용된 글</li>
	<li class="deactive">CSS 적용된 목록 글2<li>
	...
	```
	- 왜 id가 아니라 class를 사용해야 할까?
		- id는 개별자, class는 보편자니까
		- 서로 다른 태그를 grouping 할 수도 있다.
4. 부모-자식 선택자
	```html
	<style>
		ul li{
		...
		}
		lecture>li{
		border:1px solid red;
		color:blue;
		<!---lecture의 1촌 직계자손에게만 전달된다--->
		}
		ul, ol{
			background-color: powderblue;
		<!-- ','를 이용하여 ul, ol의 코드 중복을 줄일 수 있다: 공통 적용 -->
		}
	</style>
	...
	<ul>
		<li>적용</li>
		<li>되는</li>
		<li>영역</li>
	</ul>
	...
	<ol>
		<li>적용</li>
		<li>되지</li>
		<li>않음</li>
	</ol>
	...
	<ol id="lecture">
		<li>여기는 적용된다.
			<ol>
				<li>#lecture>li이면 여기는 적용되지 않는다.</li>
		<li>여기도 적용된다. (직계)</li>
	```
	- ul li: ul 하위에 있는 li에만 적용된다.
	- #lecture>li: lecture 1촌 직계자손에게만 적용된다.
		- border 속성의 경우는 직계에만 적용되지만,
		- color 속성의 경우는 하위 모두에게 적용된다: 상속과 관련
5. 가상 클래스 선택자
- elements의 상태에 따라 선택이 되기도 하고, 안 되기도 한다.
	```html
	...
	<style>
		a:visited{
			color:red;
		}
		a:active{
			color:green;
		}
		a:hover{
			color:yellow;
		}
		a:focus{
			color:white;
		}
		input:focus{
			background-color:black;
		}
	</style>
	...
	<body>
		<a href="https://naver.com">링크</a>
	</body>
	...
	```
	- 방문하였는지, 마우스를 올려 놓은 상태, 마우스를 누르고 있는 상태 등
	- 우선 순위: 태그의 위치에 따라 우선이 달라진다.
		-  focus의 경우 가장 마지막 속성으로 위치시키는 편이 낫다.
	- visited의 경우 보안 문제로 인하여 일부 속성만 사용 가능하다.
	- 어느 태그에 적용 가능한 선택자인가가 각 선택자에 따라 다르다.

### 속성
- 선택자가 주부라면, 선택자는 술부

#### font-size
- 단위(unit)개념과 연관
	- px
	- em
	- rem