共享锁和独占锁
	只能被单线程持有的锁是独占锁，可以被多线程持有的锁是共享锁。
	独占锁指在任何时候只能有一个线程持有该锁，sysronized就是一个独占锁，ReadWriteLock读写锁允许同一时间内有多个线程进行读操作，属于共享锁。
	独占锁可以理解为悲观锁，当每次访问资源时都要加上互斥锁，共享锁可以理解为了乐观锁，放宽了加锁的条件，允许多线程同时访问该资源。
	悲观锁典型的是synronized，特性是独占式互斥锁，乐观锁相对悲观锁有更好的性能，但乐观锁会带来ABA问题，可以用添加版本号来解决ABA问题，
	JDK1.5时提供了AtomicStampReference类可以解决ABA问题。
	乐观锁在提交时才进行锁定的，因此不会造成死锁。
	悲观锁指的是数据对外界的修改采取保守策略，它认为线程很容易会把数据修改掉，因此在在整个数据被修改的过程中都会采取锁定状态，直到一个线程使用完，其他线程才可继续使用。
	public class LockExample{
		public static void main(String[] args){
			synronized(LockExample.class){
				System.out,println("lock");
			}
		}
	}
	使用反编译工具查到的结果：
	Compiled from "LockExample.java"
public class com.lagou.interview.ext.LockExample {
  public com.lagou.interview.ext.LockExample();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return
 
  public static void main(java.lang.String[]);
    Code:
       0: ldc           #2                  // class com/lagou/interview/ext/LockExample
       2: dup
       3: astore_1
       4: monitorenter // 加锁
       5: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
       8: ldc           #4                  // String lock
      10: invokevirtual #5                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      13: aload_1
      14: monitorexit // 释放锁
      15: goto          23
      18: astore_2
      19: aload_1
      20: monitorexit
      21: aload_2
      22: athrow
      23: return
    Exception table:
       from    to  target type
           5    15    18   any
          18    21    18   any
}

可以看出被 synchronized 修饰的代码块，在执行之前先使用 monitorenter 指令加锁，
然后在执行结束之后再使用 monitorexit 指令释放锁资源，在整个执行期间此代码都是锁定的状态，这就是典型悲观锁的实现流程。

乐观锁和悲观锁的概念恰好相反，乐观锁认为一般情况下数据在修改时不会出现冲突，所以在数据访问之前不会加锁，
只是在数据提交更改时，才会对数据进行检测。

Java 中的乐观锁大部分都是通过 CAS（Compare And Swap，比较并交换）操作实现的，CAS 是一个多线程同步的原子指令，
CAS 操作包含三个重要的信息，即内存位置、预期原值和新值。如果内存位置的值和预期的原值相等的话，
那么就可以把该位置的值更新为新值，否则不做任何修改。
CAS 可能会造成 ABA 的问题，ABA 问题指的是，线程拿到了最初的预期原值 A，然而在将要进行 CAS 的时候，被其他线程抢占了执行权，
把此值从 A 变成了 B，然后其他线程又把此值从 B 变成了 A，然而此时的 A 值已经并非原来的 A 值了，但最初的线程并不知道这个情况，
在它进行 CAS 的时候，只对比了预期原值为 A 就进行了修改，这就造成了 ABA 的问题。

以警匪剧为例，假如某人把装了 100W 现金的箱子放在了家里，几分钟之后要拿它去赎人，然而在趁他不注意的时候，进来了一个小偷，
用空箱子换走了装满钱的箱子，当某人进来之后看到箱子还是一模一样的，他会以为这就是原来的箱子，就拿着它去赎人了，
这种情况肯定有问题，因为箱子已经是空的了，这就是 ABA 的问题。

ABA 的常见处理方式是添加版本号，每次修改之后更新版本号，拿上面的例子来说，假如每次移动箱子之后，箱子的位置就会发生变化，
而这个变化的位置就相当于“版本号”，当某人进来之后发现箱子的位置发生了变化就知道有人动了手脚，就会放弃原有的计划，
这样就解决了 ABA 的问题。

JDK 在 1.5 时提供了 AtomicStampedReference 类也可以解决 ABA 的问题，
此类维护了一个“版本号” Stamp，每次在比较时不止比较当前值还比较版本号，这样就解决了 ABA 的问题。
部分源码：
public class AtomicStampedReference<V> {
    private static class Pair<T> {
        final T reference;
        final int stamp; // “版本号”
        private Pair(T reference, int stamp) {
            this.reference = reference;
            this.stamp = stamp;
        }
        static <T> Pair<T> of(T reference, int stamp) {
            return new Pair<T>(reference, stamp);
        }
    }
    // 比较并设置
    public boolean compareAndSet(V   expectedReference,
                                 V   newReference,
                                 int expectedStamp, // 原版本号
                                 int newStamp) { // 新版本号
        Pair<V> current = pair;
        return
            expectedReference == current.reference &&
            expectedStamp == current.stamp &&
            ((newReference == current.reference &&
              newStamp == current.stamp) ||
             casPair(current, Pair.of(newReference, newStamp)));
    }
    //.......省略其他源码
}
可以看出它在修改时会进行原值比较和版本号比较，当比较成功之后会修改值并修改版本号。

	
