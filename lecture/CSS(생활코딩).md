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
	- 고정(절대) 크기: px
	- 가변(상대) 크기
		- em
		- **rem: 현재는 이걸 사용한다고 생각**
	```html
	<style>
	#px {
		font-size:16px;`
	}
	#rem {
		font-size:1rem;
	}
	</style>
	```
	- chrome 개발자 도구의 html -> computed의 폰트 사이즈를 변경해서 위를 테스트 가능

#### color
- 색상을 지정하는 3가지 방식
	- color name
	- hex
	- rgb
- 단순히 글자에만 해당하는 것이 아니라 웹에서 색상을 표현하는 경우 모두 적용됨

#### text-align
- 정렬
- text-align:
	- left
	- right
	- center
	- justify: 적합한 간격 맞춰서 채우는 방식

#### font
- font-family: arial, verdana, "Helvetica Neue" sans-serif;
	- 앞의 폰트가 없을 경우 뒤의 폰트를 적용함
	- 띄어쓰기가 있는 폰트 이름의 경우 ""으로 꼭 묶어줄 것!
	- 마지막 폰트는 포괄적인 폰트로 지정한다.
		- serif: 장식이 있음
		- sans-serif: 장식이 없음
		- cursive: 흘림체
		- fantasy
		- monospace: 고정폭
- font-weight: bold;
- line-height: 1.2(기본값)
	- 줄 간격
	- 120px처럼 픽셀 같은 고정값도 사용은 가능(권장하지 않음)
- font: 폰트와 관련된 여러 속성을 축약형으로 표현

##### web font
- 사용자가 폰트를 가지고 있지 않을 경우 브라우저에 다운로드하여 사용하는 방식
	- 용량 문제를 유의
	- 특히 한글의 경우 영문 폰트와 달리 용량이 큼
	
	

### 상속
- 특정 엘리먼트의 하위 엘리먼트도 상위 엘리먼트의 속성을 물려받는 것
	```html
	<html>
		<head>
			<style>
				html{color: red;}
				#select{color:black;}
				body{border:1px solid red;}
				/*
				li{color:red;}
				h1{color:red;}
				*/
			</style>
		</head>
		<body>
			<h1>학습 내용</h1>
			<ul>
				<li>html</li>
				<li>css</li>
				<l1 id="select">javascript</li>
			</ul>
		</body>
	</html>
	```
	- 각주 안의 내용처럼 h1와 li에 적용할 내용이 있는데 반복하는 것은 비효율적이다.
	- 둘을 포함하는 상위 태그에 해당 속성을 규정한다면 상속으로 하위 두 개 모두에 적용되서 더 경제적이다.
	- 크롬 개발자 도구의 Styles에 있는 Inherited from에서 상속 정보를 살펴볼 수 있다.
	- border의 경우 `<h1>`과 `<li>`의 상위 태그임에도 불구하고 이 하위 태그들에 속성이 상속되지 않음을 볼 수 있다: 상속되고, 되지 않는 속성이 따로 존재한다.
	
	

### Cascading
- CSS; **Cascading** Style Sheet
	- 브라우저, 사용자, 저자들에게 맞는 디자인을 적용하기 위함, 가령 접근성 등
	- 디자인 적용의 우선순위를 명확히 하는 것이 필요하다: 동일한 태그에 여러 CSS 속성이 적용된다면 어떻게 되야 하는가?
	- 웹 브라우저 < 사용자 < 저자
		- 단, 사용자의 경우 웹 페이지 디자인에 영향을 주는 일 자체가 적음
```html
<html>
	<head>
		<style>
			li{color:red;}
			#idsel{color:blue;}
			.classsel{color:green;}
		</style>
	</head>
	<body>
		<ul>
			<li>html</li>
			<li id="idsel" class="classsel" style="color:powderblue">css</li>
			<li>javascript</li>
		</ul>
	</body>
</html>
```
- 위와 같은 중첩된 상황에서 다음과 같은 우선 순위를 가진다.
	1. style attribute
	2. id selector
	3. class selector
	4. tag selector
	
- 구체적인 것일수록 우선 순위 높음, 폭넓은 것일수록 우선 순위 낮음

- `!important`는 우선순위에 있어서 지상신서이다.
	- `li{color:red !important;}`
	- 하지만 important를 사용하지 않고 적절히 우선순위를 이용하는 것이 최선이다.
	
	

### inline과 block
```html
<html>
	<head>
		<meta charset="utf-8">
		<style>
			h1, a{border:1px solid red;}
		</style>
	</head>
	<body>
		<h1>Hello, world!</h1>
		반갑습니다. <a href="https://res-cogitans.github.io">깃헙 링크</a> 달아놨습니다.
	</body>
</html>
```
- 위의 예시에서, 왜 "반갑습니다"는 해당 줄에 있건, 그 위에 줄에 있건 간에 `<h1>` 부분에서 줄바꿈이 되고, "달아놨습니다"는 줄바꿈이 안 되는 것일까?
- 동일한 border 태그를 달아줬음에도 `<h1>`은 그 줄 전체를 영역으로 삼지만, `<a>`는 그렇지 않다.
- block element와 inline element
	- `<h1>`처럼 화면 영역을 쭉 담는 것을 block level element
	- `<a>`와 같은 영역을 차지하는 것을 inline element
- inline인지 block인지를 강제할 수 있다.
	```html
	<style>
		h1{display: inline;}
		a{display:block;}
	</style>
	```



### 박스 모델

- 거리 조절: padding, margin(엘리먼트 사이의 간격!)
- width: block level element의 가로 길이도 제약 가능
- `<a>` 태그에도 위와 같은 박스 모델의 값이 적용되지만 width, height 값은 무시된다.
	- **inline의 경우 width, height는 무시됨!**