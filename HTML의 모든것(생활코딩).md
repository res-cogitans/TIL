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
<h2 id="form">form</h2>
<p>til에 업데이트</p>

