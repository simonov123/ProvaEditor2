import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class runtime {
	private String currentfile;
	private Display disp;
	private Shell sh;
	private Text editcamp;
	private ToolBar actionbar;
	private ToolBar actionbar2;
	private ToolItem save;
	private ToolItem copy;
	private ToolItem paste;
	private ToolItem cut;
	private ToolItem send;
	private ToolItem rename;
	private ToolItem delete;
	private ToolItem credits;
	private ToolItem new_;
	private ToolItem open;
	private ToolItem html;
	private provider prov;
	private ToolItem stamp;
	private CCombo font;
	private CCombo size;
	private Clipboard clip;
	
	

	public void run(String cur) {
		System.out.println("runtime");
		if(cur!=null) {
		currentfile=cur;
		prov.init(new File(currentfile), currentfile);}
		this.interfaceinit();
		prov=new provider();
		open.addListener(SWT.Selection, event->{
			 FileDialog fileDialog = new FileDialog(sh, SWT.OPEN);
		        fileDialog.setText("Seleziona un file");
			currentfile=fileDialog.open();
			sh.setText(currentfile);
			prov.init(new File(currentfile), currentfile);
			try {
				String content=prov.getcontent();
				editcamp.setText(content);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		new_.addListener(SWT.Selection, event->{
			Shell sh2=new Shell(disp);
			sh2.setText("inserire nome");
			sh2.setSize(450,120);
			GridLayout lay2=new GridLayout();
			lay2.numColumns=2;
			sh2.setLayout(lay2);
			Label name_=new Label(sh2,SWT.NONE);
			Text namecamp=new Text(sh2,SWT.BORDER|SWT.LEFT_TO_RIGHT);
			Label path_=new Label(sh2,SWT.NONE);
			Text namepath=new Text(sh2,SWT.BORDER|SWT.LEFT_TO_RIGHT);
			name_.setText("filename");
			namecamp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
			path_.setText("pathname");
			namepath.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
			Button pathchooser=new Button(sh2,SWT.BORDER);
			pathchooser.setText("scegli cartella...");
			Button confnew=new Button(sh2,SWT.BORDER);
			confnew.setText("OK");
			pathchooser.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
					
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					 DirectoryDialog chooser = new DirectoryDialog(sh2);
				     chooser.setText("Seleziona una cartella");
					String path=chooser.open();
					namepath.setText(path);
					
				}
				
			});
			confnew.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				
					
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if(currentfile!=null) {
					String content=editcamp.getText();
					try {
						prov.writecontent(content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
					String namef=namecamp.getText();
					String pathf=namepath.getText();
					currentfile=pathf+"/"+namef+".txt";
					System.out.println(currentfile);
					sh.setText(currentfile);
					prov.init(new File(currentfile), currentfile);
					try {
						prov.makefile();
						sh2.close();
						editcamp.setText("");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
				
			});
			sh2.open();
			while(sh2.isDisposed()) {
				disp.readAndDispatch();
			}
		});
		save.addListener(SWT.Selection, event->{
			String content=editcamp.getText();
			try {
				if(currentfile!=null) {
				prov.writecontent(content);}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		copy.addListener(SWT.Selection,event->{
			editcamp.copy();
		});
		paste.addListener(SWT.Selection,event->{
			editcamp.paste();
		});
		cut.addListener(SWT.Selection,event->{
			editcamp.cut();
		});
		rename.addListener(SWT.Selection,event->{
			Shell sh3=new Shell(disp);
			sh3.setSize(300,40);
			sh3.setText("rinomina");
			GridLayout lay3=new GridLayout();
			lay3.numColumns=3;
			sh3.setLayout(lay3);
			Label newnm=new Label(sh3,SWT.NONE);
			newnm.setText("nuovo nome   ");
			Text newn=new Text(sh3,SWT.BORDER);
			newn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
			Button ok2=new Button(sh3,SWT.BORDER);
			ok2.setText("OK");
			ok2.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
					
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					String newname=newn.getText();
					prov.rename(newname);
					currentfile=prov.file.getPath();
					sh.setText(currentfile);
					sh3.close();
					
				}
				
			});
			sh3.open();
			while(sh3.isDisposed()!=true) {
				disp.readAndDispatch();
			}
		});
		delete.addListener(SWT.Selection,event->{
			prov.delete();
			currentfile=null;
			sh.setText("editor");
			editcamp.setText("");
		});
		credits.addListener(SWT.Selection, event->{
			Shell sh4=new Shell(disp);
			sh4.setText("credits");
		    sh4.setSize(300,100);
		    GridLayout lay4=new GridLayout();
		    lay4.numColumns=1;
		    sh4.setLayout(lay4);
		    Label credits=new Label(sh4,SWT.NONE);
		    Button ok3=new Button(sh4,SWT.BORDER);
		    credits.setText("Красота спасет мир\n lucianosimone143@gmail.com");
		    ok3.setText("ok");
		    ok3.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
					
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					sh4.close();
					
				}
		    	
		    });
		    sh4.open();
		    while(!sh4.isDisposed()) {
		    	disp.readAndDispatch();
		    }
		});
		html.addListener(SWT.Selection,event->{
			if(currentfile!=null) {
			Shell sh5=new Shell(disp);
			sh5.setText(currentfile);
			sh5.setSize(900,600);
			sh5.setLayout(new FillLayout());
			Browser browser=new Browser(sh5,SWT.NONE);
			browser.setUrl("file:///"+prov.file.getAbsolutePath());
			browser.setJavascriptEnabled(true);
			sh5.open();
			while(!sh5.isDisposed()) {
				disp.readAndDispatch();
			}
			}});
		stamp.addListener(SWT.Selection, event->{
			if(currentfile!=null) {
				Shell sh6=new Shell(disp);
				sh6.setText("anteprima stampa");
				sh6.setSize(900,600);
				sh6.setLayout(new FillLayout());
				Browser browser=new Browser(sh6,SWT.NONE);
				 browser.addProgressListener(new ProgressAdapter() {
			            @Override
			            public void completed(ProgressEvent event) {
			                browser.execute("window.print();");
			            }
			        });
				browser.setUrl("file:///"+prov.file.getAbsolutePath());
				browser.setJavascriptEnabled(true);
				sh6.open();
				while(!sh6.isDisposed()) {
					disp.readAndDispatch();
				}
				
			}
		});
	    sh.open();
		while(!sh.isDisposed()) {
			disp.readAndDispatch();
		}
		
		
	}

	private void interfaceinit() {
		disp=new Display();
		sh=new Shell(disp);
		sh.setText("editor");
		GridLayout lay=new GridLayout();
		lay.numColumns=1;
		sh.setLayout(lay);
		editcamp=new Text(sh,SWT.LEFT_TO_RIGHT|SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
		editcamp.setSize(900, 500);
		 GridData textLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		 textLayoutData.heightHint = 500;
		 editcamp.setLayoutData(textLayoutData);
		actionbar=new ToolBar(sh,SWT.BORDER|SWT.FLAT|SWT.WRAP);
		actionbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		actionbar2=new ToolBar(sh,SWT.BORDER|SWT.FLAT|SWT.WRAP);
		actionbar2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		  save = new ToolItem(actionbar, SWT.PUSH|SWT.BORDER);
	        save.setText("Salva");
	        ToolItem separator1 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator1.setWidth(10);
	        copy = new ToolItem(actionbar, SWT.PUSH);
	        copy.setText("Copia");
	        ToolItem separator2 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator2.setWidth(10);
	        paste = new ToolItem(actionbar, SWT.PUSH);
	        paste.setText("Incolla");
	        ToolItem separator4_ = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator4_.setWidth(10);
	        cut = new ToolItem(actionbar, SWT.PUSH);
	        cut.setText("Taglia");
	        ToolItem separator3 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator3.setWidth(10);
	        send = new ToolItem(actionbar, SWT.PUSH);
	        send.setText("Invia");
	        ToolItem separator4 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator4.setWidth(10);
	        rename = new ToolItem(actionbar, SWT.PUSH);
	        rename.setText("Rinomina");
	        ToolItem separator5 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator5.setWidth(10);
	        delete = new ToolItem(actionbar, SWT.PUSH);
	        delete.setText("Elimina");
	        ToolItem separator6 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator6.setWidth(10);
	        credits = new ToolItem(actionbar, SWT.PUSH);
	        credits.setText("Crediti");
	        ToolItem separator7 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator7.setWidth(10);
	        new_ = new ToolItem(actionbar, SWT.PUSH);
	        new_.setText("Nuovo File");
	        ToolItem separator8 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator8.setWidth(10);
	        open = new ToolItem(actionbar, SWT.PUSH);
	        open.setText("Apri File");
	        ToolItem separator9 = new ToolItem(actionbar, SWT.SEPARATOR);
	        separator9.setWidth(10);
	        html = new ToolItem(actionbar, SWT.PUSH);
	        html.setText("Aprire nel Browser");
	        font=new CCombo(actionbar2, SWT.DROP_DOWN | SWT.READ_ONLY);
	        FontData[] availableFontData = disp.getFontList(null, false); // Otiene i font di sistema
	        String[] availableFonts = new String[availableFontData.length];

	        // Estrai i nomi dei font dai FontData
	        for (int i = 0; i < availableFontData.length; i++) {
	            availableFonts[i] = availableFontData[i].getName();
	        }
	        font.setItems(availableFonts);
	        font.select(0);
	        ToolItem separator10 = new ToolItem(actionbar2, SWT.SEPARATOR);
	        separator10.setWidth(10);
	        size=new CCombo(actionbar2, SWT.DROP_DOWN | SWT.READ_ONLY);
	        String[] sizedata= {"6","8","10","14","16","18","20","22","24","26","28","30","32","35","40","45","50","60","70"};
	        size.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	        for(int a=0;a<sizedata.length;a++) {
	        	size.add(sizedata[a]);
	        }
	        ToolItem separator11 = new ToolItem(actionbar2, SWT.SEPARATOR);
	        separator11.setWidth(10);
	        stamp = new ToolItem(actionbar2, SWT.PUSH|SWT.BORDER);
		     Image printico=new Image(disp,"icons/print.png");
		       stamp.setImage(printico);
		     
	        clip=new Clipboard(disp);
		if(currentfile!=null) {
			sh.setText(currentfile);
		}
		sh.setSize(900,600);
		
	}

}
