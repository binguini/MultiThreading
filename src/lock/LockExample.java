package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockExample {
	public static void main(String[] args) {
		deadLock();//死锁
		
	}

	/**
	 * 死锁
	 * 当我们在使用线程一拥有锁lock1的同时试图获取lock2,而线程二拥有锁lock2的同时试图获取lock1,会造成彼此都在等待对方释放资源，就会形成死锁
	 *锁是指在并发编程中，当有多个线程操作同一资源时，为了保证数据操作的正确性，需要让多线程排队一个一个的操作该资源，而该过程就是给资源加锁和释放锁的过程
	 */
	private static void deadLock() {
		Object lock1 = new Object();
		Object lock2 = new Object();
		//线程一拥有lock1试图获取lock2
		new Thread(() -> {
			synchronized(lock1){
			System.out.println("获取lock1成功");
			try{
				TimeUnit.SECONDS.sleep(3);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			//试图获取锁 lock2
			synchronized (lock2) {
				System.out.println(Thread.currentThread().getName());
			}
		}
		}).start();
		//线程二拥有lock2试图获取lock1
		new Thread(() -> {
			synchronized(lock2){
				System.out.println("获取lock2成功");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//试图获取锁 lock1
				synchronized(lock1){
					System.out.println(Thread.currentThread().getName());
				}
			}
		}).start();
		
	}

}
