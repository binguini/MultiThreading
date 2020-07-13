package lock;
/**
 * 
 * @author 凌轩
 * 可重入锁也叫递归锁，指的是同一个线程，如果外面的函数拥有此锁之后，内层的函数也可以继续获取该锁，
 * ReentrantLock和synchronized都是可重入锁。
 * 可重入锁的实现原来是在锁的内部存储一个线程标识，用于判断当前锁属于 哪个线程，并且锁的内部有维护了一个计数器，
 * 当锁空闲时计数器值为0，当被线程占用或者重入时分别加1，当锁被释放时计数器减1，直到减到0时表示此锁为空闲状态。
 *
 */
public class Reentrant {
	public static void main(String[] args) {
		reentrantA();
	}

	/**
	 * 可重入锁   A 方法
	 */
	private synchronized static void reentrantA() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + ":执行 reentrantA");
		reentrantB();
	}
	/**
	 * 可重入锁  B 方法
	 */
	private synchronized static void reentrantB() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + ":执行 reentrantB");
		
	}

}
/**
 * 根据执行结果可以看出reentrantA方法和reentrantB方法的执行线程都是main，调用了reentrantA方法，它的方法中嵌套了reentrantB方法，
 * 如果sysncronized是不可重入的话，那么线程会被一直阻塞。
 */

