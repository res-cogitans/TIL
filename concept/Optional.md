---


---

<p>Module java.base<br>
Package java.util</p>
<h1 id="class-optionalt">Class Optional</h1>
<p>java.lang.Object<br>
java.util.Optional<br>
Type Parameters:<br>
<code>T</code>  - the type of value</p>
<p>public final class Optional extends Object</p>
<p>A container object which may or may not contain a non-<code>null</code> value. If a value is present, <code>isPresent()</code> returns <code>true</code>. If no value is present, the object is considered <em>empty</em> and <code>isPresent()</code> returns <code>false</code>.<br>
null값을 가질 수 있는 컨테이너 오브젝트. 만약 값이 존재한다면(!=null) <code>isPresent</code>는 true를 리턴한다. 만약 어떤 값도 없다면 이 객체는 빈 것으로 여겨지며 isPresent()는 false를 리턴한다.</p>
<p>Additional methods that depend on the presence or absence of a contained value are provided, such as  [<code>orElse()</code>] (returns a default value if no value is present) and  [<code>ifPresent()</code>]  (performs an action if a value is present).<br>
추가적인 메소드들은 주어진 contained 값들의 유무에 따라 제공된다. orElse (어떤 값도 없다면 디폴트 값을 리턴)나 ifPresent() (만약 값이 존재한다면 작업을 수행한다)와 같은 메소드들이 그러하다.</p>
<p>This is a  value-based class; programmers should treat instances that are  equal as interchangeable and should not use instances for synchronization, or unpredictable behavior may occur. For example, in a future release, synchronization may fail.<br>
이는 값에 기반한 클래스다; 프로그래머들은 동일한(equal) 인스턴스들을 교환 가능한 것으로 다루어야 하며, 인스턴스들을 동기화용으로 사용하거나, 예상치못한 작용(behavior)가 발생할 수 있게끔 사용해선 안된다. 예를 들어, 향후 릴리즈에서, 동기화는 실패할 수 있겠다.</p>
<p>API Note:</p>
<p><code>Optional</code>  is primarily intended for use as a method return type where there is a clear need to represent “no result,” and where using  <code>null</code>  is likely to cause errors. A variable whose type is  <code>Optional</code>  should never itself be  <code>null</code>; it should always point to an  <code>Optional</code>  instance.<br>
Optional은 기본적으로 "결과 없음"을 명확히 표현할 필요가 있으며 null이 오류를 일으키는 경향이 있는 곳에서 메소드의 리턴 자료형으로 사용되게끔 의도되었다. Optional 자료형을 가진 변수는 그 스스로 null이어서는 안 되며 항상 Optional 인스턴스를 가리켜야 한다.</p>

