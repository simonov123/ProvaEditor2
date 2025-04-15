import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.lang.reflect.Type;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
	private ToolItem recent;
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
	private org.eclipse.swt.graphics.Font defaultFont;
	private ToolItem odtcnv;
	private ArrayList<String> rece;
	private org.eclipse.swt.widgets.List recelist;

	
	

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
			if(currentfile==null) {
				currentfile="";
			}
			sh.setText(currentfile);
			prov.init(new File(currentfile), currentfile);
			try {
				if (currentfile.endsWith(".odt")||currentfile.endsWith(".docx")) {
					Shell sh7=new Shell(disp);
					sh7.setText(currentfile + " (sola lettura)");
					sh7.setSize(900,600);
					sh7.setLayout(new FillLayout());
					Browser browser = new Browser(sh7, SWT.NONE);
					File htmlFile;
					if(currentfile.endsWith(".odt")) {
					htmlFile = new File("ODT.html");}
					else {
						htmlFile = new File("DOCX.html");
					}
					browser.setUrl(htmlFile.toURI().toString());
					Path path = Paths.get(currentfile);
					byte[] bytes = Files.readAllBytes(path);
					String base64 = Base64.getEncoder().encodeToString(bytes);
					browser.addProgressListener(new ProgressAdapter() {
					    @Override
					    public void completed(ProgressEvent event) {
					    	
					        disp.timerExec(500, () -> { // ritardo per sicurezza
					            String safeBase64 = base64.replaceAll("'", "\\\\'");
					            if(currentfile.endsWith(".odt")) {
					            String script = "window.loadODTFromBase64('" + safeBase64 + "');";
					            browser.execute(script);}
					            if(currentfile.endsWith(".docx")) {
					            	 String script = "window.loadDOCXFromBase64('" + safeBase64 + "');";
					            	 browser.execute(script);
					            }
					            
					           
					        });
					    }
					});
					new BrowserFunction(browser, "sendTextToJava") {
					    @Override
					    public Object function(Object[] arguments) {
					        if (arguments.length > 0) {
					            String textFromJS = (String) arguments[0];
					            System.out.println("Testo ricevuto da JavaScript:");
					            System.out.println(textFromJS);
					            editcamp.setText(textFromJS);
					            sh7.close();
					            // Qui puoi salvare, analizzare o usare il testo come vuoi

					            // opzionalmente puoi restituire una risposta a JS
					            return "Ricevuto in Java!";
					        }
					        return null;
					    }
					};


					sh7.open();
					while(sh7.isDisposed()!=true) {
						disp.readAndDispatch();
					}
				}
				else {
				String content=prov.getcontent();
				editcamp.setText(content);
				if(new File("data.json").exists()==true) {
					try {
				        Gson gson = new Gson();
				        FileReader reader = new FileReader("data.json");
				        Type listType = new TypeToken<List<fontdata>>() {}.getType();
				        List<fontdata> dataList = gson.fromJson(reader, listType);
				        reader.close();

				        fontdata matched = null;

				        for (fontdata data : dataList) {
				            if (data.file.equals(currentfile)) {
				                matched = data;
				        
				            }
				        }

				        if (matched != null) {
				            org.eclipse.swt.graphics.Font nuovoFont = new org.eclipse.swt.graphics.Font(
				                disp, matched.font, matched.size, SWT.NORMAL
				            );

				            int fontindex = 0;
				            int sizeindex = 0;

				            for (int i = 0; i < font.getItemCount(); i++) {
				                if (matched.font.equals(font.getItem(i))) {
				                    fontindex = i;
				                    break;
				                }
				            }

				            for (int j = 0; j < size.getItemCount(); j++) {
				                if (matched.size == Integer.parseInt(size.getItem(j))) {
				                    sizeindex = j;
				                    break;
				                }
				            }

				            font.select(fontindex);
				            size.select(sizeindex);
				            editcamp.setFont(nuovoFont);
				            System.out.println("Font applicato: " + matched.font + " " + matched.size);
				        } else {
				            System.out.println("Nessun font associato trovato per: " + currentfile);
				        }

				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}
			        editcamp.setText(content);}
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
			 String content = editcamp.getText();
			    try {
			        if (currentfile != null) {
			            prov.writecontent(content);

			            FontData[] fd = editcamp.getFont().getFontData();
			            String fontName = fd[0].getName();
			            int fontSize = fd[0].getHeight();
			            String fl = currentfile;

			            fontdata data = new fontdata(fontName, fontSize, fl);
			            Gson gson = new Gson();
			            String jsonFilePath = "data.json";

			            List<fontdata> dataList = new ArrayList<>();
			            File jsonFile = new File(jsonFilePath);

			            if (jsonFile.exists()) {
			                FileReader reader = new FileReader(jsonFile);
			                JsonElement element = JsonParser.parseReader(reader);
			                reader.close();

			                Type listType = new TypeToken<List<fontdata>>() {}.getType();

			                if (element.isJsonArray()) {
			                    dataList = gson.fromJson(element, listType);
			                } else if (element.isJsonObject()) {
			                    fontdata single = gson.fromJson(element, fontdata.class);
			                    dataList.add(single);
			                }
			            }

			            dataList.add(data); // aggiungi nuovo font associato al file

			            FileWriter writer = new FileWriter(jsonFilePath);
			            gson.toJson(dataList, writer);
			            writer.close();

			            System.out.println("Salvato: " + gson.toJson(data));
			        }
			    } catch (IOException e) {
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
		font.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        String selectedFont = font.getText();
		        int selectedSize = Integer.parseInt(size.getText().isEmpty() ? "12" : size.getText());
		        org.eclipse.swt.graphics.Font newFont = new org.eclipse.swt.graphics.Font(disp, selectedFont, selectedSize, SWT.NORMAL);
		        editcamp.setFont(newFont);
		    }
		});

		size.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        String selectedFont = font.getText();
		        int selectedSize = Integer.parseInt(size.getText());
		        org.eclipse.swt.graphics.Font newFont = new org.eclipse.swt.graphics.Font(disp, selectedFont, selectedSize, SWT.NORMAL);
		        editcamp.setFont(newFont);
		    }
		});
		recent.addListener(SWT.Selection, event -> {
			Shell sh12=new Shell(disp);
			ArrayList <String> filerecenti=new ArrayList <String>();
			try {
				filerecenti=this.fillList();
				sh12.setLayout(new FillLayout());
				sh12.setText("Recenti");
				sh12.setSize(150,400);
				recelist=new org.eclipse.swt.widgets.List(sh12,SWT.BORDER);
				for(String filerecente : filerecenti) {
					recelist.add(filerecente);
				}
				sh12.open();
				while(!sh12.isDisposed()) {
					disp.readAndDispatch();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		   });

	    sh.open();
		while(!sh.isDisposed()) {
			disp.readAndDispatch();
		}
		
		
	}

	private ArrayList<String> fillList() throws IOException {
		// TODO Auto-generated method stub
		   Gson gson = new Gson();
	        FileReader reader = new FileReader("data.json");
	        Type listType = new TypeToken<List<fontdata>>() {}.getType();
	        List<fontdata> dataList = gson.fromJson(reader, listType);
	        reader.close();
            rece=new ArrayList<String>();
	        for (fontdata data : dataList) {
	            rece.add(data.file);
	        }
		return rece;
	}

	private void interfaceinit() {
		disp=new Display();
		defaultFont = new org.eclipse.swt.graphics.Font(disp, "Arial", 14, SWT.NORMAL);
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
		 editcamp.setFont(defaultFont);
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
	        recent = new ToolItem(actionbar, SWT.PUSH);
	        recent.setText("File Recenti");
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
	        ToolItem fontItem = new ToolItem(actionbar2, SWT.SEPARATOR);
	        font = new CCombo(actionbar2, SWT.DROP_DOWN | SWT.READ_ONLY);
	        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			String [] fontNames=ge.getAvailableFontFamilyNames();
			font.setItems(fontNames);
			font.select(1);

	        // imposta larghezza e controllo
	        fontItem.setWidth(150);
	        fontItem.setControl(font);

	        // SIZE CCombo
	        ToolItem sizeItem = new ToolItem(actionbar2, SWT.SEPARATOR);
	        size = new CCombo(actionbar2, SWT.DROP_DOWN | SWT.READ_ONLY);
	        String[] sizedata = {"6","8","10","14","16","18","20","22","24","26","28","30","32","35","40","45","50","60","70"};
	        size.setItems(sizedata);
	        size.select(3); // imposta un valore iniziale

	        sizeItem.setWidth(80);
	        sizeItem.setControl(size);
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
