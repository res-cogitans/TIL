# How does a JPA Proxy work and how to unproxy it with Hibernate

[원본 링크](https://vladmihalcea.com/how-does-a-jpa-proxy-work-and-how-to-unproxy-it-with-hibernate/)

## Introduction

**개요**

In this article, I’m going to explain how JPA and Hibernate Proxy objects work, and how you can unproxy an entity Proxy to get access to the underlying POJO instance.

이 글에서, 나는 JPA와 하이버네이트의 프록시가 어떻게 작동하는가를 설명하고, 어떻게 엔티티 프록시를 비프록시화(unproxy)해서 기저에 있는 POJO 인스턴스로 접근할 수 있게끔 할 수 있는가를 설명하겠다.

The JPA lazy loading mechanism can either be implemented using Proxies or [Bytecode Enhancement](https://vladmihalcea.com/how-to-enable-bytecode-enhancement-dirty-checking-in-hibernate/) so that calls to lazy associations can be intercepted and relationships initialized prior to returning the result back to the caller.

JPA의 지연로딩 작동방식은 프록시나 bytecode enhancement 둘 중 무엇으로도 이전에 초기화된 lazy 연관관계를 가로채고, 값이 호출되어 반환되기 전에 초기화되게끔 구현될 수 있다.

Initially, in JPA 1.0, it was assumed that Proxies should not be a mandatory requirement, and that’s why `@ManyToOne` and `@OneToOne` associations use an EAGER loading strategy by default. However, [EAGER fetching is bad for performance](https://vladmihalcea.com/eager-fetching-is-a-code-smell/) so it’s better to use the `FetchType.LAZY` fetching strategy for all association types.

초기 JPA 1.0에서는 프록시가 의무적인 요구사항은 아니었다고 상정되었다. 그래서 `@ManyToOne`과 `OneToOne`관계는 EAGER 로딩 전략을 기본으로 삼는 것이다. 하지만 EAGER fetch 전략은 성능이 나빠서 `FetchType.LAZY`를 모든 연관관계 종류에 대해서 fetch 전략으로 사용하는 것이 더 낫다.

In this article, we are going to see how the proxy mechanism works and how you can unproxy a given Proxy to the actual entity.

이 글에서, 우리는 어떻게 프록시 매커니즘이 작동하는가, 그리고 어떻게 프록시를 비프록시화(unproxy)하여 실제 엔티티로 만들 수 있는가를 볼 것이다.



## Loading a Proxy with JPA and Hibernate

**프록시를 JPA와 하이버네이트로 로딩하기**

The JPA `EntityManager` defines two ways to load a given entity.

JPA `EntityManager`는 주어진 엔티티에 대해 두 가지 로딩 방식을 정의한다.

When calling the [`find`](http://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html#find-java.lang.Class-java.lang.Object-) method, the entity is going to be loaded either from the first-level cache, second-level cache or from the database. Therefore, the returned entity is of the same type with the declared entity mapping.

find 메서드를 호출할 때, 엔티티는 1차캐시(first-level cache)에서 로딩될 수도, 2차 캐시(second-level cache) 혹은 데이터베이스에서 로딩될 수도 있다. 따라서 반환되는 엔티티는 선언된 엔티티 매핑과 동일한 타입이다.

On the contrary, when calling the [`getReference`](http://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html#getReference-java.lang.Class-java.lang.Object-) method, the returned object is a Proxy and not the actual entity object type. The benefit of returning a Proxy is that we can initialize a parent `@ManyToOne` or `@OneToOne` association without having to hit the database when we justs want to set a Foreign Key column with a value that we already know.

반면에 getReference 메서드를 사용할 때, 반환된 객체는 프록시이며 실제 엔티티 객체 타입이 아니다. 프록시를 반환하는 것의 이득은, 우리가 이미 알고 있는 외래 키 컬럼 값을 보고 싶을 때, 데이터베이스에 접근(hit)하지 않고도 부모 `@ManyToOne`이나 `@OneToOne` 연관관계를 초기화할 수 있다는 점이다.

So, when running the following example:

그래서 다음 예제를 작동시킬 때:



```java
Post post = entityManager.getReference(Post.class, 1L);
 
PostComment comment = new PostComment();
comment.setId(1L);
comment.setPost(post);
comment.setReview("A must read!");
entityManager.persist(comment);

```



Hibernate is going to issue a single INSERT statement without needing to execute any SELECT statement:

하이버네이트는 어떤 SELECT 구문을 실행할 필요 없이 INSERT 구문을 보낼 수 있다.



```sql
INSERT INTO post_comment (post_id, review, id)
VALUES (1, 'A must read!', 1)
```



While this example underlines when Proxies are useful for writing data, Proxies are very convenient for reading data as well.

한편 이 예제는 프록시가 데이터를 쓸 때도 읽을 때도 유용하다는 것을 강조한다.

Considering we have the following `PostComment` entity:

다음과 같은 `PostComment`엔티티를 고려해보자:



```java
@Entity(name = "PostComment")
@Table(name = "post_comment")
public class PostComment {
 
    @Id
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
 
    private String review;
 
    //Getters and setters omitted for brevity
}
```



When executing the following test case:

아래와 같은 테스트 케이스를 실행한다면:



```java
PostComment comment = entityManager.find(
    PostComment.class,
    1L
);
 
LOGGER.info("Loading the Post Proxy");
 
assertEquals(
    "High-Performance Java Persistence",
    comment.getPost().getTitle()
);
```



Hibernate generates the following output:

하이버네이트는 다음 결과를 생성한다.



```SQL
SELECT pc.id AS id1_1_0_,
       pc.post_id AS post_id3_1_0_,
       pc.review AS review2_1_0_
FROM   post_comment pc
WHERE  pc.id = 1
 
-- Loading the Post Proxy
 
SELECT p.id AS id1_0_0_,
       p.title AS title2_0_0_
FROM   post p
WHERE  p.id = 1
```



The first SELECT statement fetches the `PostComment` entity without initializing the parent `Post` association since it was marked with `FetchType.LAZY`. By inspecting the selected FOREIGN KEY column, Hibernate knows whether to set the `post` association to `null` or to a Proxy. If the FOREIGN KEY column value is not null, then the Proxy will only populate the association identifier.

첫 번째 SELECT 구문은 부모 `Post` 연관관계를 초기화하지 않고, `PostComment` 엔티티를 fetch한다. `FetchType.Lazy`로 설정되었기 때문이다. 선택된 FK 컬럼을 검토함으로써 하이버네이트는 `Post`연관관계를 `null`로 설정할 지, 프록시로 설정할 지를 안다. 만약 FK 컬럼이 null이 아니라면 프록시는 연관관계 식별자를 그저 덧붙일 뿐이다.

However, when accessing the `title` attribute, Hibernate needs to issue a secondary SELECT statement to initialize the `Post` Proxy.

하지만 `title` 속성에 접근할 때, 하이버네이트는 `Post` 프록시를 초기화하기 위해서 두 번째 SELECT 구문을 보낼 필요가 있다.



## How to unproxy a Proxy object with JPA and Hibernate

**JPA와 하이버네이트에서 어떻게 프록시 오브젝트를 비프록시화할 수 있을까**

As we have already seen, by navigating the Proxy object, Hibernate issues the secondary SELECT statement and initializes the association. Hence the Proxy is replaced by the actual entity object.

우리가 이미 본 바와 같이, 프록시 객체를 살펴봄으로써 하이버네이트는 두 번째 SELECT 구문을 보내고 연관관계를 초기화한다. 그러므로 프록시는 실제 엔티티 오브젝트로 대체된다.

Considering that the `Post` entity is mapped as follows:

`Post` 엔티티가 다음과 같이 매핑되었다 생각해보자:



```java
@Entity(name = "Post")
@Table(name = "post")
public class Post {
 
    @Id
    private Long id;
 
    private String title;
 
    //Getters and setters omitted for brevity
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
         
        return id != null && id.equals(((Post) o).getId());
    }
 
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```



When executing the following test case:

다음의 테스트 케이스를 실행한다면:



```java
Post _post = doInJPA(entityManager -> {
    Post post = new Post();
    post.setId(1L);
    post.setTitle("High-Performance Java Persistence");
    entityManager.persist(post);
    return post;
});
 
doInJPA(entityManager -> {
    Post post = entityManager.getReference(Post.class, 1L);
    LOGGER.info(
        "Post entity class: {}",
        post.getClass().getName()
    );
 
    assertFalse(_post.equals(post));
 
    assertTrue(
        _post.equals(Hibernate.unproxy(post))
    );
});
```



Hibernate generates the following output:

하이버네이트는 다음 결과를 생성한다.



```java
Post entity class: com.vladmihalcea.book.hpjp.hibernate.fetching.HibernateProxyTest$Post_$$_jvst8fd_0
```



Because the Proxy object class is a dynamically generated type, so the Proxy `post` object is not equal to the `_post` object which is an actual `Post` class instance.

프록시 객체 클래스는 동적으로 생성된 타입이기 때문에, 프록시 `Post` 객체는 실제 `Post`클래스 인스턴스인 `_post` 객체와는 다르다.

However, after calling the `unproxy` method, introduced in [Hibernate 5.2.10](https://hibernate.atlassian.net/browse/HHH-10831), the original `_post` entity and the unproxied `post` object are equal.

Prior to Hibernate 5.2.10, to unproxy an object without traversing it, you’d have to execute the following logic:

하지만 하이버네이트 5.2.10에서 나오는 `unproxy` 메서드를 호출하고 나면 원본 `_post` 엔티티와 비프록시화된 `post` 객체는 동등(equal)하다. 하이버네이트 5.2.10 이전에는 그것을 순회하지(traversing) 않고 객체를 비프록시화하려면 다음과 같은 로직을 실행했어야만 했다:



```java
Object unproxiedEntity = null;
 
if(proxy instanceof HibernateProxy) {
    HibernateProxy hibernateProxy = (HibernateProxy) proxy;
    LazyInitializer initializer =
        hibernateProxy.getHibernateLazyInitializer();
    unproxiedEntity = initializer.getImplementation();
}
```



Not very nice, right? Luckily, starting with Hibernate ORM 5.2.10, you can unproxy a Hibernate Proxy with the [`Hibernate#unproxy`](http://docs.jboss.org/hibernate/orm/5.2/javadocs/org/hibernate/Hibernate.html#unproxy-java.lang.Object-) utility method:

그리 썩 좋지 않다. 운 좋게도 하이버네이트 ORM 5.2.10부터, 하이버네이트 프록시를 `Hibernate.unproxy` 유틸리티 메서드를 이용해서 비프록시화할 수 있다.



```java
Object unproxiedEntity = Hibernate.unproxy(proxy);
```



Much better!

훨씬 낫다!



## Conclusion

**결론**

Understanding Hibernate internals can make a difference between an application that barely crawls and one that runs at warp speed. Lazy associations are very important from a performance perspective, and you really have to understand how Proxies work since you will inevitably bump into them on a daily basis.

하이버네이트의 내부를 이해하는 것은 겨우 작동하는 어플리케이션과, 매우 빠른 속도의 어플리케이션 만큼의 차이를 만들 수 있다. 지연 연관관계는 성능 관점에서 매우 중요하며, 일상적으로 그것들을 만날 것이기 때문에 어떻게 프록시가 작동하는 지 알아야만 한다.

