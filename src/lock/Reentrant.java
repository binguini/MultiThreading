package lock;
/**
 * 
 * @author ����
 * ��������Ҳ�еݹ�����ָ����ͬһ���̣߳��������ĺ���ӵ�д���֮���ڲ�ĺ���Ҳ���Լ�����ȡ������
 * ReentrantLock��synchronized���ǿ���������
 * ����������ʵ��ԭ�����������ڲ��洢һ���̱߳�ʶ�������жϵ�ǰ������ �ĸ��̣߳����������ڲ���ά����һ����������
 * ��������ʱ������ֵΪ0�������߳�ռ�û�������ʱ�ֱ��1���������ͷ�ʱ��������1��ֱ������0ʱ��ʾ����Ϊ����״̬��
 *
 */
public class Reentrant {
	public static void main(String[] args) {
		reentrantA();
	}

	/**
	 * ��������   A ����
	 */
	private synchronized static void reentrantA() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + ":ִ�� reentrantA");
		reentrantB();
	}
	/**
	 * ��������  B ����
	 */
	private synchronized static void reentrantB() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + ":ִ�� reentrantB");
		
	}

}
/**
 * ����ִ�н�����Կ���reentrantA������reentrantB������ִ���̶߳���main��������reentrantA���������ķ�����Ƕ����reentrantB������
 * ���sysncronized�ǲ�������Ļ�����ô�̻߳ᱻһֱ������
 */

