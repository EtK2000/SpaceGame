package com.etk2000.SideThreads;

import java.util.ArrayList;

public class SideThread {
	private static ArrayList<SideThread> threads = new ArrayList<>();
	protected boolean running, stopSignal = false;// only allows thread to run once
	protected Thread thread;
	protected Runnable run;

	public SideThread() {
	}

	public SideThread(Runnable run) {
		this.run = run;
	}

	public void Init(SideThread thread) {
		if (running)
			return;
		running = true;
		addRunningThread(thread);
		this.thread = new Thread(run);
		this.thread.start();
	}

	public void stopThread() {
		stopSignal = true;
	}

	public static Runnable killAll() {// TODO: make more dynamic
		return new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < threads.size(); i++)
					threads.get(i).stopThread();
			}
		};
	};

	protected void addRunningThread(SideThread thread) {
		threads.add(thread);
	}
	
	public static void registerThread(Runnable runnable) {
		SideThread st = new SideThread(runnable);
		threads.add(st);
		st.Init(st);
	}
	
	public static void registerThread(Thread thread) {
		SideThread st = new SideThread(thread);
		threads.add(st);
		st.Init(st);
	}
}