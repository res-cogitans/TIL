---


---

<h1 id="기본을-확실히-html의-모든것생활코딩">기본을 확실히!! HTML의 모든것(생활코딩)</h1>
<ul>
<li><strong>H</strong>yper<strong>T</strong>ext<strong>M</strong>arkup<strong>L</strong>anguage<br>
: 하이퍼텍스트(링크) + 마크업 문법/형식 + 언어</li>
<li>1990, 팀 버너스 리 창시<br>
GML(1960말)-&gt; SGML-&gt;SGMLguid(19개태그)-&gt;HTML(+1태그(<code>&lt;a&gt;</code>) )</li>
</ul>
<h1 id="html-기본-문법과-기본-태그">HTML 기본 문법과 기본 태그</h1>
<h2 id="태그">태그</h2>
<p>마크업과 태그는 밀접한 관계를 가짐(태그의 중요성)</p>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>strong</span><span class="token punctuation">&gt;</span></span>강조하고 싶은 텍스트<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>strong</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>&lt;시작태그&gt; &lt;/닫히는 태그&gt;구조.</li>
<li></li>
</ul>
<h2 id="속성">속성</h2>
<ul>
<li><code>a</code>는 anchor 태그</li>
</ul>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>a</span> <span class="token attr-name">href</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>https://github.com/res-cogitans/res-cogitans.github.io<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
내 깃헙 저장소<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>a</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<a href="https://github.com/res-cogitans/res-cogitans.github.io" target="blank" title="html은 금방 배운다">
내 깃헙 저장소</a>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>a</span> <span class="token attr-name">href</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>속성값<span class="token punctuation">"</span></span> <span class="token attr-name">target</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>속성값<span class="token punctuation">"</span></span> <span class="token attr-name">title</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>Html은 금방배운다<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span> <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>a</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>속성의 순서는 무관하다.</li>
</ul>
<h2 id="list-태그">List 태그</h2>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>ul</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>li</span><span class="token punctuation">&gt;</span></span>밀레토스학파<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>li</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>li</span><span class="token punctuation">&gt;</span></span>엘레아학파<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>li</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>li</span><span class="token punctuation">&gt;</span></span>아테네 철학자<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>li</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>ul</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>ol</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>li</span><span class="token punctuation">&gt;</span></span>탈레스<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>li</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>li</span><span class="token punctuation">&gt;</span></span>아낙시만드로스<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>li</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>li</span><span class="token punctuation">&gt;</span></span>아낙시메네스<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>li</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>ol</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>형이상학</li>
<li>인식론</li>
<li>윤리학</li>
</ul>
<ol>
<li>탈레스</li>
<li>아낙시만드로스</li>
<li>아낙시메네스</li>
</ol>
<ul>
<li><code>&lt;li&gt;</code>태그: 리스트. (list)</li>
<li><code>&lt;ol&gt;</code>태그: 순서 있는 리스트(ordered list)</li>
<li><code>&lt;ul&gt;</code>태그: 그룹화(unordered list)</li>
</ul>
<h2 id="문서의-구조">문서의 구조</h2>
<hr>
<p><code>&lt;html&gt;</code><br>
<code>&lt;head&gt;</code><br>
<code>&lt;title&gt;철학개론&lt;/title&gt;</code> 이 부분은 상위에 떠 있을 제목<br>
<code>&lt;meta charset="utf-8"&gt;</code><br>
<code>&lt;/head&gt;</code><br>
-&gt; 문서</p>
<p><code>&lt;body&gt;</code></p>
<h3> 철학의 대분류 </h3>
<ul>
<li>형이상학</li>
<li>인식론</li>
<li>윤리학</li>
</ul>
<h4>실천철학과 이론철학 구별법</h4>
<p><code>&lt;/body&gt;</code><br>
<code>&lt;/html&gt;</code><br>
-&gt; 이 부분은 본문에 해당한다.</p>
<hr>
<p><strong>html문서의 구조</strong><br>
<code>&lt;html&gt;</code>+ <code>&lt;head&gt;</code> + <code>&lt;body&gt;</code></p>
<h4 id="doctype">DOCTYPE</h4>
<p><code>&lt;!DOCTYPE html&gt;</code>: Document type declaration</p>
<h2 id="p태그와-br태그"><code>&lt;p&gt;</code>태그와 <code>&lt;br&gt;</code>태그</h2>
<ul>
<li>html은 줄바꿈을 무시함. 단락 표시를 위해선 단락 전후에 <code>&lt;p&gt;</code>태그 필요.</li>
<li><code>&lt;p&gt;</code>태그의 단락 띄우는 크기를 바꾸려면 css를 이용.</li>
<li><code>&lt;br&gt;</code>태그: 줄바꿈 태그</li>
<li><code>&lt;br&gt;</code>태그는 void element다. (열리는 태그만 필요)</li>
<li><code>&lt;p&gt;</code>태그는 단락을 묶는 의미라면, <code>&lt;br&gt;</code>태그는 시각적 줄바꿈.</li>
</ul>
<h2 id="img태그"><code>&lt;img&gt;</code>태그</h2>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>img</span> <span class="token attr-name">src</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>sampleImage.jpg<span class="token punctuation">"</span></span> <span class="token attr-name">width</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>320<span class="token punctuation">"</span></span> <span class="token attr-name">height</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>240<span class="token punctuation">"</span></span>
<span class="token attr-name">alt</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>샘플 이미지<span class="token punctuation">"</span></span> <span class="token attr-name">title</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>마우스를 올리면 볼 수 있는 표현<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>alt는 이미지가 깨졌을 경우 표시됨. alternative text.</li>
</ul>
<h2 id="표-만들기table">표 만들기(table)</h2>
<ul>
<li><code>&lt;table border="2"&gt;</code> 태그, border 속성</li>
<li><code>&lt;td&gt;</code>태그: table data. 테이블의 각 항목을 묶어주면 됨.</li>
<li><code>&lt;tr&gt;</code>태그: table row. td를 묶어줌(행)</li>
</ul>
&gt;<table border="3">
<thead>
<tr>
<th>분류1</th><th>분류2</th><th>분류3</th><th>값</th>
</tr>
</thead>
<tbody>
<tr>
<td>항목1</td><td>항목2</td><td rowspan="2">항목3</td><td>500</td>
</tr>
<tr>
<td>항목4</td><td>항목5</td><td>1000</td>
</tr></tbody><tfoot>
<tr><td colspan="3">합계</td><td>1500</td>
</tr></tfoot>


</table>
<ul>
<li>테이블의 테두리 형태 등은 css등을 추가 사용하여 꾸밀 수 있음.</li>
<li>과거에는 레이아웃을 만드는 데 사용했었음.</li>
<li><code>&lt;thead&gt;</code>와 <code>&lt;tbody&gt;</code> <code>&lt;th&gt;``&lt;tfoot&gt;</code>데이터 구조화를 위한 수단</li>
<li><code>&lt;thead&gt;</code>나 <code>&lt;tfoot&gt;</code>의 위치는 알아서 정리됨.<br>
-병합 관련 속성:  rowspan=“value”: 수직결합, colspan=“value”: 수평결합</li>
</ul>
<hr>
<h2 id="form-입력-양식">form: 입력 양식</h2>
<h3 id="문자-입력">문자 입력</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/login.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>아이디: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>id<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>default<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>비밀번호: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>password<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>pwd<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>주소: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>address<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>P</span><span class="token punctuation">&gt;</span></span>textarea:
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>textarea</span> <span class="token attr-name">cols</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>50<span class="token punctuation">"</span></span> <span class="token attr-name">rows</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>2<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>default value<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>textarea</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li><code>&lt;input type="type" name="name" value="default value"&gt;</code> 형태임.</li>
<li><code>&lt;input type="submit"&gt;</code>은 버튼</li>
<li><code>&lt;textarea cols="value" rows="value"&gt;</code>넓은 텍스트 입력필드,<br>
기본값 입력이 <code>&lt;input type&gt;</code>형태와는 다름에 유의.</li>
</ul>
<hr>
<h3 id="dropdown-list">Dropdown List</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/color.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>h1</span><span class="token punctuation">&gt;</span></span>색상<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>h1</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>select</span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>color<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>option</span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>red<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>붉은색<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>option</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>option</span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>green<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>초록색<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>option</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>option</span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>blue<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>파란색<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>option</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>select</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>

<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/color-multiple.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>h1</span><span class="token punctuation">&gt;</span></span>색상(다중선택)<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>h1</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>select</span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>color<span class="token punctuation">"</span></span> <span class="token attr-name">multiple</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>option</span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>red<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>붉은색<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>option</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>option</span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>green<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>초록색<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>option</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>option</span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>blue<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>파란색<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>option</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>select</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>multiple 경우 ctrl 누르고 해야 다중선택가능.</li>
</ul>
<hr>
<h3 id="radio-button-check-box">Radio Button, Check Box</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>localhost/grading.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>h2</span><span class="token punctuation">&gt;</span></span>평가 점수 선택<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>h2</span><span class="token punctuation">&gt;</span></span>
		만족: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>radio<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>grade<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>good<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		보통: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>radio<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>grade<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>normal<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		불만: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>radio<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>grade<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>bad<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>

<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>localhost/paperweight.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>h2</span><span class="token punctuation">&gt;</span></span>전자문진<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>h2</span><span class="token punctuation">&gt;</span></span>
		발열: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>checkbox<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>paperweight<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>fever<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		인후통: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>checkbox<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>paperweight<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>sore-throat<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		설사: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>checkbox<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>paperweight<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>diarrhea<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>

</code></pre>
<hr>
<h3 id="button">button</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/form.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>전송<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>button<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>버튼<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>reset<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>버튼은 html만 순전히 사용하는 것이라면 의미 없다. Js등 프로그래밍 언어 사용시에 의미 있다.</li>
<li>reset 입력값 초기화</li>
</ul>
<hr>
<h3 id="hidden-field">hidden field</h3>
<ul>
<li>ui를 거치거나 드러내지 않고 서버로 데이터를 전송하는 방식.</li>
</ul>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/form.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>id<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>hidden<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>hide<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>testhidden<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>submit 시에 hide=testhidden가 전달됨.</li>
</ul>
<hr>
<h3 id="label">label</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/login.html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>label</span> <span class="token attr-name">for</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>id_txt<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>아이디<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>label</span><span class="token punctuation">&gt;</span></span>:
		 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">id</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>id_txt<span class="token punctuation">"</span></span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>id<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>default<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>label</span> <span class="token attr-name">for</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>pwd_txt<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>비밀번호<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>label</span><span class="token punctuation">&gt;</span></span>:
		 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">id</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>pwd_txt<span class="token punctuation">"</span></span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>password<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>pwd<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>label</span> <span class="token attr-name">for</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>add_txt<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>주소<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>label</span><span class="token punctuation">&gt;</span></span>:
		 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">id</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>add_txt<span class="token punctuation">"</span></span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>address<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>label</span><span class="token punctuation">&gt;</span></span>textarea:
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>textarea</span> <span class="token attr-name">cols</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>50<span class="token punctuation">"</span></span> <span class="token attr-name">rows</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>2<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>default value<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>textarea</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>label</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>label</span><span class="token punctuation">&gt;</span></span>
		가입안내 여부: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>checkbox<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>introduced<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>true<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>label</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>label</span> <span class="token attr-name">for</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>is_pvilged<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		혜택대상 여부: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">id</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>is_pvilged<span class="token punctuation">"</span></span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>checkbox<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>priviliged<span class="token punctuation">"</span></span> <span class="token attr-name">value</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>true<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>label</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>label을 통해 보다 명확한 정보 전달</li>
<li>label for와 input id 일치를 하면,<br>
label 선택(클릭)을 통해 관련 form으로 이동하게 함. (직관적)</li>
</ul>
<p><strong>방법2</strong><br>
<code>&lt;label&gt;</code> <code>&lt;/label&gt;</code>로 input이나 textarea를 감싸는 방식</p>
<hr>
<h3 id="파일-업로드">파일 업로드</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>http://localhost/upload.html<span class="token punctuation">"</span></span> <span class="token attr-name">method</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>post<span class="token punctuation">"</span></span> <span class="token attr-name">enctype</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>multipart/form-data<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>id<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>file<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>profile<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<hr>
<h2 id="정보로서의-html-검색엔진-최적화">정보로서의 HTML, 검색엔진 최적화</h2>
<p>SEO(Search Engine Optimization)</p>
<h3 id="font-태그">font 태그</h3>
<p><strong>사용하지 마라!!!</strong></p>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>body</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>font</span> <span class="token attr-name">size</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>5<span class="token punctuation">"</span></span> <span class="token attr-name">color</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>red<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>Hello<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>font</span><span class="token punctuation">&gt;</span></span>world
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>body</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<p><font size="5" color="red">Hello</font>world</p>
<ul>
<li>이 태그는 시각적인 정보일 뿐이지, 어떠한 정보도 주지 않는다.</li>
<li>디자인용 태그가 그때 그때 나오면서 용량을 잡아먹음</li>
<li>디자인 코드와 정보가 서로 혼재되어 버림.<br>
-&gt; HTML은 <strong>정보</strong>로서 중요!<br>
-&gt; <strong>CSS</strong> 개발하여 디자인 분리</li>
</ul>
<hr>
<h3 id="meta-태그">meta 태그</h3>
<ul>
<li>웹페이지 전체에 대한 데이터, 데이터에 대한 메타적 데이터</li>
<li>요약, 검색 키워드 등</li>
</ul>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>html</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>head</span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>meta</span> <span class="token attr-name">charset</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>utf-8<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>meta</span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>description<span class="token punctuation">"</span></span> <span class="token attr-name">content</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>메타데이터 수업내용<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>meta</span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>keywords<span class="token punctuation">"</span></span> <span class="token attr-name">content</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>메타, meta, 태그, html<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
		<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>meta</span> <span class="token attr-name">http-equiv</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>refresh<span class="token punctuation">"</span></span> <span class="token attr-name">content</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>30<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>head</span><span class="token punctuation">&gt;</span></span>
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>body</span><span class="token punctuation">&gt;</span></span>
	메타에 대한 정보가 메타데이터
	<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>body</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>html</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li><code>charset="utf-8"</code>: 인코딩</li>
<li>이것을 꺼내 표현: 디코딩(거칠게 말해서)</li>
<li>description 검색 요약 자료</li>
<li>keywords, author 등 속성들도.</li>
<li><code>&lt;meta http-equiv="refresh" content="30"&gt;</code>30초 간격으로 새로고침.</li>
</ul>
<h3 id="의미론적인semantic-태그">의미론적인(semantic) 태그</h3>
<ul>
<li><code>&lt;header&gt;</code> <code>&lt;/header&gt;</code> 실질적인 변화는 없지만, header: 전체 내용을 표현하는 부분을 표현해주는 것.</li>
<li><code>&lt;footer&gt;</code> <code>&lt;/footer&gt;</code> 개인보호정책, 회사소개 등</li>
<li><code>&lt;nav&gt;</code> <code>&lt;/nav&gt;</code> 페이지 컨텐츠 탐색하는 네비게이션 역할을 해준다.</li>
<li><code>&lt;article&gt;</code> <code>&lt;/article&gt;</code> 본문, 본문 여럿을 <code>&lt;section&gt;</code> <code>&lt;/section&gt;</code>으로 묶어줄수도. 각 섹션은 각각 단위. 애매한 것들을 통째로 묶기도.</li>
</ul>
<h3 id="검색-엔진-최적화">검색 엔진 최적화</h3>
<ul>
<li>html 코드를 의미론적으로 잘 쓰는 것이 핵심.</li>
<li>Search Engine Optimization</li>
<li><code>&lt;title&gt;</code>태그 사용 적절히, 각 페이지마다 달리</li>
<li><code>&lt;meta name="description=" content="페이지 설명 내용" &gt;</code> 검색 시 메타 태그 부분 나옴</li>
<li>url 구조 개선: 개선을 통해 검색엔진이 문서를 크롤링하기 좋게 만들 수 있다.
<ul>
<li>피해야 할 것들:
<ul>
<li>불필요한 매개변수와 세션 ID가 있는 긴 url: url에 알 수 없는 매개변수가 너무 많으면 그 부분을 빠트려서 링크가 깨지는 경우도 있음.</li>
<li><code>page1.htm</code>l과 같은 페이지 이름 선택</li>
<li><code>car-seat-car-seat-car-seat-car-seat.html</code>과 같은 과도하게 반복적인 키워드 사용</li>
</ul>
</li>
<li>단순 디렉토리 구조 만들기: 다음은 피해라
<ul>
<li><code>...dir1/dir2/dir3/.../page.html</code>과 같은 깊이 중첩된 하위 디렉토리 사용</li>
<li>내용과 관련 없는 디렉토리 이름 사용</li>
</ul>
</li>
<li>특정 문서에 도달하기 위한 한 가지 형태의 url 제공:
<ul>
<li>url 인지도 분산을 피하기 위함</li>
<li><code>&lt;link rel="canonical" href="link~"&gt;</code> 동일성표시(표준url지정)</li>
<li>여러 url로 같은 페이지 접속시 301리다이렉션을 이용하라</li>
</ul>
</li>
<li>사이트 내 이동을 쉽게: 크롤링은 하이퍼텍스트를 이용하여 이동: 조직적 링크
<ul>
<li>사이드 이동 경로의 제공으로 편리하게 만들기 1&gt;2&gt;3&gt;4 표시</li>
<li>사용자가 url의 일부를 제공하는 경우를 고려</li>
<li>실사용자와 검색엔진을 위한 두 종류의 사이트맵 준비</li>
<li>이동경로를 위해 텍스트 링크를 제공하라(검색엔진은 자바스크립트를 통한 이동 등은 이해 못할 수도 있다)</li>
</ul>
</li>
<li>양질의 콘텐츠/서비스
<ul>
<li>검색엔진을 위한 것이 아닌 사용자를 위한 컨텐츠 작성: 과도하게 검색엔진만을 위해 작성시에 웹스팸으로 분류될 수 있다.</li>
</ul>
</li>
<li>보다 나은 앵커 텍스트 작성
<ul>
<li>함축적, 간결한, 유관한  텍스트</li>
<li>피해야 할 작성법
<ul>
<li>페이지, 문서, 여기를 클릭 같은 형태(정보전달에 도움이 안 됨)</li>
<li>url을 대부분의 앵커 텍스트로 이용</li>
<li>링크를 눈에 띄기 쉽게 표현</li>
</ul>
</li>
</ul>
</li>
<li>이미지 사용의 최적화
<ul>
<li>alt 태그를 이용하라: 웹 접근성에도 도움이 됨</li>
<li>/images 등 단일 디렉토리에 일반적인 방식으로 저장하라</li>
<li>의미 있는 이미지, 짧은 파일명, 적당한 alt(너무 길거나, 전체 문장은 안 됨)</li>
</ul>
</li>
<li>제목 태그 적절히 사용</li>
<li>robot.txt 를 효과적으로 활용
<ul>
<li>크롤링 제어. 검색에 필요하지 않은 부분을 제어 -&gt; 트래픽 조절</li>
<li><code>/robots.txt</code>
<ul>
<li>Disallow 부분은 불필요하거나, 검색을 원치 않는 부분들</li>
<li>이는 보안 도구로서는 부적함 - 민감한 콘텐츠는 더 안전한 방식 사용</li>
<li><code>Sitemap: /sitemap</code> 기계가 이해하기 쉬운 사이트맵</li>
</ul>
</li>
<li>페이지랭크:
<ul>
<li>많은 사이트가 가리킬수록 페이지랭크가 증가</li>
<li>페이지랭크가 높은 사이트가 링크할수록 페이지랭크가 높음</li>
<li>웹의 핵심은 하이퍼링크</li>
</ul>
</li>
</ul>
</li>
</ul>
</li>
</ul>
<h3 id="웹개발자-도구--모바일-지원">웹개발자 도구 / 모바일 지원</h3>
<p>웹개발자 도구로 기기별 뷰를 볼 수도 있음<br>
<code>&lt;meta name="viewport" content="width=device-width, initial-scale=1.0"&gt;</code></p>
<h2 id="html5">HTML5</h2>
<h3 id="새로운-제출-양식들">새로운 제출 양식들</h3>
<ul>
<li>input types: 어떤 타입만 입력 가능하게, 모바일에서는 input type에 맞게 입력하기 편한 방식제공. 유효하지 않은 값 입력을 제한
<ul>
<li>color</li>
<li>date</li>
<li>datetime</li>
<li>datetime-local</li>
<li>email</li>
<li>month</li>
<li>number<br>
<code>&lt;input type="number" min="10" max="15"&gt;</code></li>
<li>range</li>
<li>search</li>
<li>tel</li>
<li>time</li>
<li>url</li>
<li>week</li>
</ul>
</li>
</ul>
<h3 id="입력폼의-새로운-속성들">입력폼의 새로운 속성들</h3>
<ul>
<li>자동완성
<ul>
<li><code>&lt;form action="login.php" autocomplete="on"&gt;</code></li>
</ul>
</li>
<li>placeholder: 칸에 대한 설명
<ul>
<li>`</li>
</ul>
</li>
<li><code>&lt;. . . autofocus&gt;</code>: 자동적으로 포커싱이 오게</li>
</ul>
<h3 id="입력-값-체크">입력 값 체크</h3>
<p>유효성 검사validation: 입력값의 유효성, 안전성 검사</p>
<ul>
<li>위에서 다뤘던 input의 type값은 유효성 검사 역할을 하기도 함</li>
<li>입력 필수값은 <code>required</code> 속성을 넣으면 됨</li>
<li><code>pattern="정규표현식"</code></li>
</ul>

