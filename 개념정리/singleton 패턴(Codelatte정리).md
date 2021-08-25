# singleton 패턴(Codelatte정리)

```java
public class Configuration {
	private static Configuration configuration;

	private Configuration() {
	}

	public static Configuration getInstance() {
		if (null == configuration) {
			configuration = new Configuration();
		}
		return configuration;
}

    public String getCafeName() {
        return cafeName;
    }

    public String getCafeAddress() {
        return cafeAddress;
    }
}
```

- Configuration.getInstatnce()로만 인스턴스 생성 가능.
- 정적 메소드 → Method Area 메모리 공간 차지!
반면 Singleton은 인스턴스 지연 생성(Lazy Load), 메모리 절약

```java
public void free() {
        configuration = null;
```

- Garbage Collector가 수집하게 하여 극한의 메모리 아끼기 가능.