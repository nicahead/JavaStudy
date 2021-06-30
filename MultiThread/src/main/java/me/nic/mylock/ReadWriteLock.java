package me.nic.mylock;

/**
 * 使用wait-notify实现一个Lock
 * 读写锁
 * 读读共享, 读写互斥, 写写互斥
 */
// 锁的接口
interface MyLock04 {
    void lock();

    void unlock();
}

// 用于同步
class Sync {
    // 当前占有锁的线程id，没有线程占有的时候是-1
    private long owner = -1;

    // 读锁=0，写锁=1，无锁=-1
    private int type = -1;

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

// 读写锁
class ReadWriteLock {
    private MyLock04 readLock;
    private MyLock04 writelock;
    // 同步状态
    private Sync sync;

    ReadWriteLock() {
        sync = new Sync();
        readLock = new Readlock(sync);
        writelock = new Writelock(sync);
    }

    // 读锁
    public class Readlock implements MyLock04 {

        private Sync sync;

        Readlock(Sync sync) {
            this.sync = sync;
        }

        @Override
        public void lock() {
            synchronized (sync) {
                // 其他线程使用写锁，需要等待
                while (sync.getType() == 1 && sync.getOwner() != -1) {
                    try {
//                        System.out.printf("thread %s 等待读锁...%n", Thread.currentThread().getId());
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 此时锁对象被释放，之前等待的线程可以去竞争获得锁对象
                sync.setOwner(Thread.currentThread().getId());
                // 设置为读锁
                sync.setType(0);
//            sync.setCount(sync.getCount() + 1);
//                System.out.printf("thread %s 获得读锁...%n", Thread.currentThread().getId());
            }
        }

        @Override
        public void unlock() {
            synchronized (sync) {
                // 锁对象的占有设为null
                sync.setOwner(-1);
                sync.setType(-1);
//                System.out.printf("thread %s 释放读锁...%n", Thread.currentThread().getId());
                sync.notifyAll();
            }
        }
    }

    public MyLock04 getReadLock() {
        return this.readLock;
    }

    // 写锁
    public class Writelock implements MyLock04 {

        private Sync sync;

        Writelock(Sync sync) {
            this.sync = sync;
        }

        @Override
        public void lock() {
            synchronized (sync) {
                // 只要有锁就要阻塞，无论是读锁还是写锁
                while (sync.getOwner() != -1) {
                    try {
//                        System.out.printf("thread %s 等待写锁...%n", Thread.currentThread().getId());
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 此时锁对象被释放，之前等待的线程可以去竞争获得锁对象
                sync.setOwner(Thread.currentThread().getId());
                sync.setType(1);
//                System.out.printf("thread %s 获得写锁...%n", Thread.currentThread().getId());
            }
        }

        @Override
        public void unlock() {
            synchronized (sync) {
                // 锁对象的占有设为null
                sync.setOwner(-1);
                sync.setType(-1);
//                System.out.printf("thread %s 释放写锁...%n", Thread.currentThread().getId());
                sync.notifyAll();
            }
        }
    }

    public MyLock04 getWritelock() {
        return this.writelock;
    }
}
