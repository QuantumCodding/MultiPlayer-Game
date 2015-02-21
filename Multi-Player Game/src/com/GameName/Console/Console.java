package com.GameName.Console;

import java.io.File;
import java.util.ArrayList;

import com.GameName.Console.Base.BasicFileViewer;
import com.GameName.Console.Base.ConsoleTab;
import com.GameName.Console.Base.ConsoleWindow;
import com.GameName.Console.Base.Logger;
import com.GameName.Engine.GameEngine;

public class Console extends ConsoleWindow {
	private static final long serialVersionUID = -3976580901753175960L;
	private final GameEngine ENGINE;
	
	private GameNameLogTab log;
	private TagEditorTab editor;
	private ThreadStatusTab thread;
	private BasicFileViewer errorLog;
	
	public Console(GameEngine eng) {
		super();
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);		
		ENGINE = eng;
		
		log = new GameNameLogTab(ENGINE);
		editor = new TagEditorTab();
		thread = new ThreadStatusTab();
		errorLog = new BasicFileViewer("Error Log", 
				ConsoleTab.createImageIcon("res/textures/Console/ErrorLogIcon.png"));
		errorLog.setDefaultLocation(new File("res"));
		
		addTab(log); addTab(editor); addTab(thread); addTab(errorLog);
		Logger.setLog(log);
	}

	protected void killInstance() { 
		ENGINE.getGameName().stop();
	}
	
	public ArrayList<ConsoleTab> getTabs() { return tabListArray; }
	
	public GameNameLogTab getLog() { return log; }
	public TagEditorTab getEditor() { return editor; }
	public ThreadStatusTab getThread() { return thread; }
	public BasicFileViewer getErrorLog() { return errorLog; }
}
