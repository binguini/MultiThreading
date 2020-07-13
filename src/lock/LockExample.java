package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockExample {
	public static void main(String[] args) {
		deadLock();//����
		
	}

	/**
	 * ����
	 * ��������ʹ���߳�һӵ����lock1��ͬʱ��ͼ��ȡlock2,���̶߳�ӵ����lock2��ͬʱ��ͼ��ȡlock1,����ɱ˴˶��ڵȴ��Է��ͷ���Դ���ͻ��γ�����
	 *����ָ�ڲ�������У����ж���̲߳���ͬһ��Դʱ��Ϊ�˱�֤���ݲ�������ȷ�ԣ���Ҫ�ö��߳��Ŷ�һ��һ���Ĳ�������Դ�����ù��̾��Ǹ���Դ�������ͷ����Ĺ���
	 */
	private static void deadLock() {
		Object lock1 = new Object();
		Object lock2 = new Object();
		//�߳�һӵ��lock1��ͼ��ȡlock2
		new Thread(() -> {
			synchronized(lock1){
			System.out.println("��ȡlock1�ɹ�");
			try{
				TimeUnit.SECONDS.sleep(3);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			//��ͼ��ȡ�� lock2
			synchronized (lock2) {
				System.out.println(Thread.currentThread().getName());
			}
		}
		}).start();
		//�̶߳�ӵ��lock2��ͼ��ȡlock1
		new Thread(() -> {
			synchronized(lock2){
				System.out.println("��ȡlock2�ɹ�");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//��ͼ��ȡ�� lock1
				synchronized(lock1){
					System.out.println(Thread.currentThread().getName());
				}
			}
		}).start();
		
	}

}
