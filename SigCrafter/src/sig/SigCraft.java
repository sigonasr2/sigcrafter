package sig;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import sig.skills.BasicSynthesis;
import sig.skills.BasicTouch;
import sig.skills.BrandOfTheElements;
import sig.skills.ByregotsBlessing;
import sig.skills.GreatStrides;
import sig.skills.HastyTouch;
import sig.skills.InnerQuiet;
import sig.skills.Innovation;
import sig.skills.MastersMend;
import sig.skills.NameOfTheElements;
import sig.skills.Observe;
import sig.skills.RapidSynthesis;
import sig.skills.StandardTouch;
import sig.skills.TricksOfTheTrade;
import sig.skills.Veneration;
import sig.skills.WasteNot;
import sig.skills.WasteNotII;
import sig.utils.FileUtils;

public class SigCraft {
	public static int LEVEL = 100;
	public static int RECIPE_LEVEL = 41;
	public static int CP = 280;
	public static int BASE_PROGRESS = 57;
	public static int CONTROL = 185;
	public static int PROGRESS_GOAL = 143;
	public static int QUALITY_GOAL = 2109;
	public static boolean GUARANTEED = true;
	public static int DURABILITY = 80;
	
	public static int NORMAL_COUNT = 0;
	public static int GOOD_COUNT = 0;
	public static int EXCELLENT_COUNT = 0;
	public static boolean STAT_TRACKING = true;
	final static NumberFormat displayFormat = new DecimalFormat("#0.0");
	
	public static List<String> VALID_TOUCH_ACTIONS = Arrays.asList("Basic Touch","Standard Touch","Hasty Touch","Byregot's Blessing","Brand of the Elements");
	
	public static Map<String,Buff> BUFFLIST = new HashMap<String,Buff>();
	public static Map<String,Skill> SKILLLIST = new HashMap<String,Skill>();
	
	public static int CRAFT_PROGRESS = 0;
	public static int CRAFT_QUALITY = 0;
	public static int CRAFT_DURABILITY = 0;
	public static int CRAFT_CP = 0;
	
	public static boolean CONDITION_CHECK = true;

	public static List<Skill> PROGRESS_ROTATION = new ArrayList<Skill>();
	
	public static Craft BEST_CRAFT;
	
	public static Robot r;
	
	public static ColorPosition CRAFTING_WINDOW_PIXELS = new ColorPosition(284,68,77,77,77);
	public static ColorPosition CRAFT_START_PIXELS = new ColorPosition(334,130,74,77,74);
	public static ColorPosition PRACTICE_CRAFT_START_PIXELS = new ColorPosition(334,130,41,40,41);
	public static ColorPosition READY_FOR_ACTION_PIXELS = new ColorPosition(1031,892,230,197,164);
	
	//Quality = (0.37 * Control + 32.6) * (1 - 0.05 * min(max(Recipe Level - Character Level, 0), 5))
	//Good +50%, Excellent +300%
	
	//Fail conditions: Progress does not reach 100% when durability reaches 0
	
	public static ArrayList<Craft> SucceededCrafts = new ArrayList<Craft>();
	
	public static Craft CURRENT_CRAFT;
	
	public static Condition CURRENT_CONDITION;

	public static void main(String[] args) {
		
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		SKILLLIST.put("Rapid Synthesis",new RapidSynthesis("Rapid Synthesis",0,false,9,KeyEvent.VK_SHIFT,KeyEvent.VK_4));
		SKILLLIST.put("Basic Synthesis",new BasicSynthesis("Basic Synthesis",0,true,1,KeyEvent.VK_1));
		SKILLLIST.put("Brand of the Elements",new BrandOfTheElements("Brand of the Elements",6,true,37,KeyEvent.VK_CONTROL,KeyEvent.VK_X));
		SKILLLIST.put("Basic Touch",new BasicTouch("Basic Touch",18,true,5,KeyEvent.VK_2));
		SKILLLIST.put("Hasty Touch",new HastyTouch("Hasty Touch",0,false,9,KeyEvent.VK_CONTROL,KeyEvent.VK_3));
		SKILLLIST.put("Standard Touch",new StandardTouch("Standard Touch",32,true,18,KeyEvent.VK_SHIFT,KeyEvent.VK_2));
		//SKILLLIST.add(new Skill("Byregot's Blessing",24,true,50)); //TODO We don't know how this works yet.
		SKILLLIST.put("Tricks of the Trade",new TricksOfTheTrade("Tricks of the Trade",0,true,13,KeyEvent.VK_CONTROL,KeyEvent.VK_2));
		SKILLLIST.put("Master's Mend",new MastersMend("Master's Mend",88,true,7,KeyEvent.VK_3));
		SKILLLIST.put("Waste Not",new WasteNot("Waste Not",56,true,15,KeyEvent.VK_CONTROL,KeyEvent.VK_1));
		SKILLLIST.put("Waste Not II",new WasteNotII("Waste Not II",98,true,47,KeyEvent.VK_CONTROL,KeyEvent.VK_4));
		SKILLLIST.put("Inner Quiet",new InnerQuiet("Inner Quiet",18,true,11,KeyEvent.VK_5));
		SKILLLIST.put("Veneration",new Veneration("Veneration",18,true,15,KeyEvent.VK_SHIFT,KeyEvent.VK_1));
		SKILLLIST.put("Great Strides",new GreatStrides("Great Strides",32,true,21,KeyEvent.VK_CONTROL,KeyEvent.VK_5));
		SKILLLIST.put("Innovation",new Innovation("Innovation",18,true,26,KeyEvent.VK_4));
		SKILLLIST.put("Name of the Elements",new NameOfTheElements("Name of the Elements",30,true,37,KeyEvent.VK_CONTROL,KeyEvent.VK_Z));
		SKILLLIST.put("Observe",new Observe("Observe",7,true,13,KeyEvent.VK_C));
		SKILLLIST.put("Byregot's Blessing",new ByregotsBlessing("Byregot's Blessing",24,true,50,KeyEvent.VK_ALT,KeyEvent.VK_2));
		
		
		BUFFLIST.put("Inner Quiet",new Buff("Inner Quiet",0));
		BUFFLIST.put("Veneration",new Buff("Veneration",0));
		BUFFLIST.put("Great Strides",new Buff("Great Strides",0));
		BUFFLIST.put("Innovation",new Buff("Innovation",0));
		BUFFLIST.put("Name of the Elements",new Buff("Name of the Elements",0));
		BUFFLIST.put("Name of the Elements Has Been Used",new Buff("Name of the Elements Has Been Used",0));
		BUFFLIST.put("Inner Quiet",new Buff("Inner Quiet", 0));
		BUFFLIST.put("Waste Not",new Buff("Waste Not",0));
		
		//SetupCraft();
		
		String[] data = FileUtils.readFromFile("condition_stats.txt");
		NORMAL_COUNT = Integer.parseInt(data[0]);
		GOOD_COUNT = Integer.parseInt(data[1]);
		EXCELLENT_COUNT = Integer.parseInt(data[2]);
		
		//284,68 77,77,77
		while (true) {
			r.delay(100);
			//LookForScreenPixels(CRAFT_START_PIXELS);
			//System.out.println(GetCondition());
			CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
			try {
				Color col=null;
				LookForScreenPixels(CRAFTING_WINDOW_PIXELS);
				r.delay(150);
				System.out.println("Detected crafting window...");
				BufferedImage img;
				int attempts=0;
				do {
					PressKey(KeyEvent.VK_NUMPAD0);r.delay(300);
					img = CaptureScreen();
					if (++attempts>20) {PressKey(KeyEvent.VK_ESCAPE);r.delay(300);PressKey(KeyEvent.VK_ESCAPE);r.delay(300);System.exit(1);}
				} while (new Color(img.getRGB(CRAFTING_WINDOW_PIXELS.p.x,CRAFTING_WINDOW_PIXELS.p.y)).equals(CRAFTING_WINDOW_PIXELS.c));
				//334,130 74,77,74
				LookForScreenPixels(CRAFT_START_PIXELS,PRACTICE_CRAFT_START_PIXELS);
				System.out.println("Craft started...");
				//336,267 151,220,96
				LookForScreenPixels(READY_FOR_ACTION_PIXELS);
				CONDITION_CHECK = true;
				UpdateCondition();
				LoadRotation_40Durability_800Quality_1Synth_280CP_LV50();
				//LoadRotation_40Durability_1200Quality_1Synth_282CP_LV47();
				//LoadRotation_40Durability_1200Quality_1Synth_wVeneration_280CP_LV47();
				//LoadRotation_40Durability_1700Quality_1Synth_278CP_LV47();
				//LoadRotation_40Durability_1700Quality_1Synth_wVeneration_278CP_LV47();
				//LoadRotation_40Durability_1900Quality_1Synth_280CP_LV45();
				//LoadRotation_40Durability_1900Quality_1Synth_280CP_LV50();
				//LoadRotation_40Durability_2300Quality_1Synth_282CP_LV50();
				System.out.println("Rotation: "+CURRENT_CRAFT.getRotationString());
				FileUtils.writetoFile(new String[]{Integer.toString(NORMAL_COUNT),Integer.toString(GOOD_COUNT),Integer.toString(EXCELLENT_COUNT),},"condition_stats.txt");
				int TOTAL_COUNT = NORMAL_COUNT+GOOD_COUNT+EXCELLENT_COUNT;
				System.out.println("  Condition Stats: NORMAL ("+NORMAL_COUNT+"): "+(displayFormat.format(((double)NORMAL_COUNT/TOTAL_COUNT)*100))+"%   GOOD ("+GOOD_COUNT+"): "+(displayFormat.format(((double)GOOD_COUNT/TOTAL_COUNT*100)))+"%   EXCELLENT ("+EXCELLENT_COUNT+"): "+(displayFormat.format(((double)EXCELLENT_COUNT/TOTAL_COUNT)*100))+"%");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			//break;
		}
	}

	private static void LoadRotation_40Durability_2300Quality_1Synth_282CP_LV50() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		boolean tricksUsed = false;
		tricksUsed=CheckForRecoveryCP()||tricksUsed;
		PerformSkill("Waste Not II");
		while (CURRENT_CRAFT.craft_durability>10&&!MaxQuality()) {
			if (CURRENT_CONDITION==Condition.EXCELLENT) {
				PerformSkill("Basic Touch",true);
				PerformSkill("Innovation",true);
				while (CURRENT_CRAFT.craft_durability>10&&CURRENT_CRAFT.BuffList.get("Waste Not").stackCount>=3) {
					UseRegularTouch();
				}
				PerformSkill("Great Strides",true);
				PerformSkill("Byregot's Blessing",true);
				PerformSkill("Basic Synthesis");
				return;
			}
			PerformSkill("Hasty Touch",true);
		}
		PerformSkill("Master's Mend",true);
		//tricksUsed=CheckForRecoveryCP()||tricksUsed;
		PerformSkill("Hasty Touch",true);
		tricksUsed=CheckForRecoveryCP()||tricksUsed;
		PerformSkill("Hasty Touch",true);
		if (CheckForRecoveryCP()) {
			if (!tricksUsed) {
				tricksUsed=true;
				UseRegularTouch();
			}
		}
		PerformSkill("Innovation",true);
		if (CURRENT_CRAFT.craft_cp>=84) {
			UseRegularTouch();
		} else {
			CheckForRecoveryCP();PerformSkill("Hasty Touch",true);
		}
		PerformSkill("Great Strides",true);
		if (CURRENT_CRAFT.craft_cp>=24) {
			while (CURRENT_CRAFT.BuffList.get("Great Strides").stackCount>=2&&CURRENT_CRAFT.BuffList.get("Innovation").stackCount>=2&&CURRENT_CRAFT.craft_cp>=31&&CURRENT_CONDITION!=Condition.GOOD&&CURRENT_CONDITION!=Condition.EXCELLENT) {
				PerformSkill("Observe",true);
			}
			PerformSkill("Byregot's Blessing",true);
			
		} else {
			UseRegularTouch();
		}
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_2300Quality_2Synth_282CP_LV50() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		boolean tricksUsed = false;
		tricksUsed=CheckForRecoveryCP()||tricksUsed;
		PerformSkill("Waste Not II");
		while (CURRENT_CRAFT.craft_durability>10&&!MaxQuality()) {
			PerformSkill("Hasty Touch",true);
		}
		PerformSkill("Master's Mend",true);
		//tricksUsed=CheckForRecoveryCP()||tricksUsed;
		PerformSkill("Hasty Touch",true);
		tricksUsed=CheckForRecoveryCP()||tricksUsed;
		PerformSkill("Hasty Touch",true);
		if (tricksUsed) {
			PerformSkill("Hasty Touch",true);
		}
		if (CheckForRecoveryCP()) {
			if (!tricksUsed) {
				tricksUsed=true;
				PerformSkill("Hasty Touch",true);
			}
		}
		PerformSkill("Great Strides",true);
		if (CheckForRecoveryCP()) {
			if (!tricksUsed) {
				tricksUsed=true;
				PerformSkill("Hasty Touch",true);
			}
		}
		PerformSkill("Innovation",true);
		if (CURRENT_CRAFT.craft_cp>=24) {
			PerformSkill("Byregot's Blessing",true);
		} else {
			UseRegularTouch();
		}
		if (tricksUsed) {
			PerformSkill("Veneration");
		}
		if (CURRENT_CRAFT.craft_cp>=18) {
			tricksUsed=true;
			PerformSkill("Hasty Touch",true);
		}
		PerformSkill("Basic Synthesis");
		if (!tricksUsed) {
			PerformSkill("Basic Synthesis");
		}
	}

	private static void LoadRotation_40Durability_800Quality_1Synth_280CP_LV50() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		CONDITION_CHECK = false;UpdateCondition();
		PerformSkill("Waste Not II");
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		PerformSkill("Great Strides",true);
		PerformSkill("Byregot's Blessing",true);
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_1900Quality_1Synth_280CP_LV50() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		CONDITION_CHECK = false;UpdateCondition();
		PerformSkill("Waste Not II");
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		PerformSkill("Great Strides",true);
		PerformSkill("Byregot's Blessing",true);
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_1700Quality_1Synth_278CP_LV47() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		CONDITION_CHECK = false;UpdateCondition();
		PerformSkill("Innovation");
		PerformSkill("Waste Not II");
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		CONDITION_CHECK = true;UpdateCondition();
		PerformSkill("Innovation",true);
		CONDITION_CHECK = false;UpdateCondition();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_1700Quality_1Synth_wVeneration_278CP_LV47() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		CONDITION_CHECK = false;UpdateCondition();
		PerformSkill("Innovation");
		PerformSkill("Waste Not II");
		UseRegularTouch();
		UseRegularTouch();
		CONDITION_CHECK = true;UpdateCondition();
		PerformSkill("Innovation",true);
		CONDITION_CHECK = false;UpdateCondition();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		PerformSkill("Veneration");
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_1200Quality_1Synth_282CP_LV47() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		CONDITION_CHECK = false;UpdateCondition();
		PerformSkill("Inner Quiet");
		PerformSkill("Innovation");
		PerformSkill("Waste Not II");
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		//PerformSkill("Innovation",true);
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_1200Quality_1Synth_wVeneration_280CP_LV47() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		PerformSkill("Inner Quiet");
		CONDITION_CHECK = false;UpdateCondition();
		PerformSkill("Innovation");
		PerformSkill("Waste Not II");
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		//PerformSkill("Innovation",true);
		UseRegularTouch();
		UseRegularTouch();
		UseRegularTouch();
		PerformSkill("Veneration");
		PerformSkill("Basic Synthesis");
	}

	private static void LoadRotation_40Durability_1900Quality_1Synth_280CP_LV45() {
		DURABILITY=40;
		CURRENT_CRAFT = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		boolean combo=false;
		PerformSkill("Inner Quiet");
		CheckForRecoveryCP();PerformSkill("Innovation");
		CheckForRecoveryCP();PerformSkill("Waste Not");
		while (!MaxQuality()) {
			if (CURRENT_CONDITION==Condition.EXCELLENT) {
				UseRegularTouch();
			} else
			if (CURRENT_CRAFT.BuffList.get("Waste Not").stackCount!=0) {
				if (!IsThereEnoughTurns(CURRENT_CRAFT.craft_durability,CURRENT_CRAFT.BuffList,1)) {
					break;
				}
				UseRegularTouch();
			} else {
				CheckForRecoveryCP();PerformSkill("Innovation");
				CheckForRecoveryCP();PerformSkill("Waste Not");
			}
			//System.out.println(IsThereEnoughTurns(CURRENT_CRAFT.craft_durability,CURRENT_CRAFT.BuffList,2));
		}
		PerformSkill("Basic Synthesis");
	}

	private static void UseRegularTouch() {
		if ((CURRENT_CRAFT.craft_durability>5&&CURRENT_CRAFT.BuffList.get("Waste Not").stackCount>0)||(CURRENT_CRAFT.craft_durability>10&&CURRENT_CRAFT.BuffList.get("Waste Not").stackCount==0)) {
			if (CURRENT_CRAFT.craft_cp>=18) {
				if (IsCombo()) {
					PerformSkill("Standard Touch",true);
				} else {
					PerformSkill("Basic Touch",true);
				}
			} else {
				PerformSkill("Hasty Touch",true);
			}
		}
	}

	private static boolean IsCombo() {
		return CURRENT_CRAFT.SkillList.get(CURRENT_CRAFT.SkillList.size()-1).name.equals("Basic Touch");
	}

	private static boolean CheckForRecoveryCP() {
		if (CURRENT_CONDITION==Condition.GOOD) {PerformSkill("Tricks of the Trade");}
		return CURRENT_CONDITION==Condition.GOOD;
	}
	
	private static void UpdateCondition() {
		//160,282 255,255,255 NORMAL
		//160,282 255,194,214 GOOD
		//160,282 <150,<150,<150 POOR
		//ELSE EXCELLENT
		if (!CONDITION_CHECK&&!STAT_TRACKING) {
			CURRENT_CONDITION=Condition.NORMAL;
			return;
		}
		try {
			r.delay(50);
			LookForScreenPixels(READY_FOR_ACTION_PIXELS,CRAFTING_WINDOW_PIXELS);
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			Color col = new Color(CaptureScreen().getRGB(160, 282));
			//System.out.println(col);
			if (col.getRed()<=150&&col.getGreen()<=150&&col.getBlue()<=150) {
				CURRENT_CONDITION = Condition.POOR;
				return;
			} else 
			if (col.getRed()>=235&&col.getGreen()>=130&&col.getGreen()<=214&&col.getBlue()>=194&&col.getBlue()<=234) {
				CURRENT_CONDITION = Condition.GOOD;
				if (STAT_TRACKING) {GOOD_COUNT++;}
				return;
			} else 
			if (col.getRed()>=245&&col.getGreen()>=245&&col.getBlue()>=245) {
				CURRENT_CONDITION = Condition.NORMAL;
				if (STAT_TRACKING) {NORMAL_COUNT++;}
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		CURRENT_CONDITION = Condition.EXCELLENT;
		if (STAT_TRACKING) {EXCELLENT_COUNT++;}
		return;
	}
	
	private static void PerformSkill(String string) {
		PerformSkill(string,false);
	}

	private static void PerformSkill(String string,boolean checkForMaxQuality) {

		if (CURRENT_CONDITION==Condition.EXCELLENT&&
				!VALID_TOUCH_ACTIONS.contains(string)) {
			UseRegularTouch();
		}
		try {
			LookForScreenPixels(READY_FOR_ACTION_PIXELS);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		r.delay(40);
		if (checkForMaxQuality&&MaxQuality()) {
			return;
		}
		if (VALID_TOUCH_ACTIONS.contains(string)&&!IsThereEnoughTurns(CURRENT_CRAFT.craft_durability,CURRENT_CRAFT.BuffList,1)) {return;}
		//System.out.println("   Still enough Turns.");
		Skill s = SKILLLIST.get(string);
		if (s==null) {
			System.err.println("Could not find skill "+string+"!");
			System.exit(1);
		}
		if (s.modifier!=-1) {
			PressKeyWithModifier(s.modifier,s.key);
		} else {
			PressKey(s.key);
		}
		if (CURRENT_CRAFT.craft_cp>=s.CPCost) {
			s.useSkill(CURRENT_CRAFT);
			System.out.println("   Durability: "+CURRENT_CRAFT.craft_durability+"  CP:"+CURRENT_CRAFT.craft_cp);
		}
		r.delay(300);
		UpdateCondition();
		if (CONDITION_CHECK) {
			System.out.println("Condition is now: "+CURRENT_CONDITION);
		}
		//1031,892 115,98,82 230,197,164
	}
 
	private static boolean MaxQuality() {
		Color col=null;
		try {
			col = new Color(CaptureScreen().getRGB(336, 267));
			//System.out.println(col);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return col.getRed()==151&&col.getGreen()==220&&col.getBlue()==96;
	}

	private static void LookForScreenPixels(ColorPosition...cp) throws IOException, InterruptedException {
		Color col;
		boolean found=false;
		do {
			BufferedImage screen = CaptureScreen();
			for (ColorPosition cpp : cp) {
				col = new Color(screen.getRGB(cpp.p.x, cpp.p.y));
				if (cpp.c.equals(col)) {
					found=true;
					break;
				}
			}
			r.delay(10);
		} while (!found);
	}

	private static BufferedImage CaptureScreen() throws IOException {
		BufferedImage screenshot = r.createScreenCapture(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		//ImageIO.write(screenshot,"png",new File("screenshot.png"));
		return screenshot;
	}
	
	private static void PressKey(int keycode) {
		r.keyPress(keycode);
		r.keyRelease(keycode);
	}
	
	private static void PressKeyWithModifier(int modifier,int keycode) {
		r.keyPress(modifier);
		r.keyPress(keycode);
		r.keyRelease(keycode);r.delay(100);
		r.keyRelease(modifier);
	}
	
	public static boolean IsThereEnoughTurns(int durability,Map<String,Buff> bufflist,int turnsRequired) {
		Buff wasteNot = bufflist.get("Waste Not");
		int turnsRemaining = (int)(durability%2==1&&wasteNot.stackCount%2==1?Math.ceil(((double)wasteNot.stackCount/2))-1:Math.ceil((double)wasteNot.stackCount/2))+(int)Math.ceil(durability/10)+(durability%2==1?1:0);
		int maxHalfTurnsRemaining = durability/5;
		turnsRemaining = Math.min(maxHalfTurnsRemaining,turnsRemaining);
		System.out.println("Turns Remaining: "+turnsRemaining);
		return turnsRemaining>turnsRequired;
	}
	
	public static void SetupCraft() {
		
		List<Skill> progress_rotation1 = new ArrayList<Skill>();
		int progressSteps = 0;
		int PROGRESS_FINAL_STEPS = 0;
		List<Skill> progress_rotation2 = new ArrayList<Skill>();
		int qualityCPRemaining = 0;

		Craft c1 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		//Tame rotation attempt.
		while (c1.craft_progress<c1.progress_goal) {
			Skill s = SKILLLIST.get("Basic Synthesis");
			s.useSkill(c1);
			progress_rotation1.add(s);
		}
		System.out.println("Basic Synthesis by itself takes "+progress_rotation1.size()+" turns.");
		Craft c2 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		//Veneration rotation attempt.
		Skill s = SKILLLIST.get("Veneration");
		s.useSkill(c2);
		progress_rotation2.add(s);
		while (c2.craft_progress<c2.progress_goal) {
			if (c2.BuffList.get("Veneration").stackCount==0) {
				s = SKILLLIST.get("Veneration");
				s.useSkill(c2);
				progress_rotation2.add(s);
			}
			s = SKILLLIST.get("Basic Synthesis");
			s.useSkill(c2);
			progress_rotation2.add(s);
			progressSteps++;
		}
		System.out.println("Basic Synthesis with Veneration takes "+progressSteps+" turns, consuming "+(c2.cp-c2.craft_cp)+" CP.");
		if (progressSteps<progress_rotation1.size()) {
			PROGRESS_ROTATION.addAll(progress_rotation2);
			qualityCPRemaining = c2.craft_cp;
			PROGRESS_FINAL_STEPS = progressSteps;
			System.out.println("\tUsing Veneration rotation for Progress. Quality CP Available: "+qualityCPRemaining);
		} else {
			PROGRESS_ROTATION.addAll(progress_rotation1);
			qualityCPRemaining = c1.craft_cp;
			PROGRESS_FINAL_STEPS = progress_rotation1.size();
			System.out.println("\tUsing standard rotation for Progress. Quality CP Available: "+qualityCPRemaining);
		}
		
		RunCraftCheck(new String[]{},new String[]{"Basic Touch"},new String[]{});

		boolean combo=false;
		//Can we just straight up craft?
		for (int i=0;i<2;i++) {
			List<Skill> quality_rotation1 = new ArrayList<Skill>();
			Craft c3 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
			combo=false;
			while (c3.craft_quality<c3.quality_goal && IsThereEnoughTurns(c3.craft_durability,c3.BuffList,PROGRESS_FINAL_STEPS)) {
				s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
				s.useSkill(c3);
				quality_rotation1.add(s);
				combo=!combo;
				if (i==1) {combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, quality_rotation1, c3, combo);}
			}
			System.out.println("Raw Quality Rotation leaves "+c3.craft_cp+" CP, "+c3.craft_durability+" durability, "+(c3.quality_goal-c3.craft_quality)+" quality from the goal.");
			if (i==0&&c3.craft_cp<=SKILLLIST.get("Master's Mend").CPCost) {break;}
		}
		
		//Add Inner Quiet
		List<Skill> quality_rotation2 = new ArrayList<Skill>();
		Craft c4 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(c4);
		quality_rotation2.add(s);
		combo=false;
		while (c4.craft_quality<c4.quality_goal && IsThereEnoughTurns(c4.craft_durability,c4.BuffList,PROGRESS_FINAL_STEPS)) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(c4);
			quality_rotation2.add(s);
			combo=!combo;
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, quality_rotation2, c4, combo);
		}
		System.out.println("Raw Quality + Inner Quiet Rotation leaves "+c4.craft_cp+" CP, "+c4.craft_durability+" durability, "+(c4.quality_goal-c4.craft_quality)+" quality from the goal.");
		
		//Add Innovation
		List<Skill> quality_rotation3 = new ArrayList<Skill>();
		Craft c5 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		List<Skill> current_rotation = quality_rotation3;
		Craft current_craft = c5;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Innovation");
		s.useSkill(current_craft);
		current_rotation.add(s);
		combo=false;
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
				combo=false;
			}
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		//System.out.println(current_rotation);
		System.out.println("Raw Quality + Inner Quiet + Innovation Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");

		//Add Waste Not
		List<Skill> quality_rotation4 = new ArrayList<Skill>();
		Craft c6 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation4;
		current_craft = c6;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Innovation");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not");
		s.useSkill(current_craft);
		current_rotation.add(s);
		combo=false;
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
				combo=false;
			}
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		//System.out.println(current_rotation);
		System.out.println("Raw Quality + Inner Quiet + Innovation + Waste Not (once) Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");
		
		//No Innovation
		List<Skill> quality_rotation5 = new ArrayList<Skill>();
		Craft c7 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation5;
		current_craft = c7;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not");
		s.useSkill(current_craft);
		current_rotation.add(s);
		combo=false;
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Raw Quality + Inner Quiet + Waste Not (once) Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");

		
		//Add Waste Not II
		List<Skill> quality_rotation6 = new ArrayList<Skill>();
		Craft c8 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation6;
		current_craft = c8;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Innovation");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not II");
		s.useSkill(current_craft);
		current_rotation.add(s);
		combo=false;
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
				combo=false;
			}
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Raw Quality + Inner Quiet + Innovation + Waste Not II (once) Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");
		
		//No Innovation
		List<Skill> quality_rotation7 = new ArrayList<Skill>();
		Craft c9 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation7;
		current_craft = c9;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not II");
		s.useSkill(current_craft);
		current_rotation.add(s);
		combo=false;
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Raw Quality + Inner Quiet + Waste Not II (once) Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");
				
		//Hasty Touch versions
		List<Skill> quality_rotation8 = new ArrayList<Skill>();
		Craft c10 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation8;
		current_craft = c10;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not II");
		s.useSkill(current_craft);
		current_rotation.add(s);
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Waste Not").stackCount==0) {
				s = SKILLLIST.get("Waste Not II");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Hasty Touch + Inner Quiet + Waste Not II Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, ~"+(int)Math.floor((current_craft.quality_goal-current_craft.craft_quality)*0.6)+" quality from the goal.");

		List<Skill> quality_rotation9 = new ArrayList<Skill>();
		Craft c11 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation9;
		current_craft = c11;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not II");
		s.useSkill(current_craft);
		current_rotation.add(s);
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			if (current_craft.BuffList.get("Waste Not").stackCount==0) {
				s = SKILLLIST.get("Waste Not II");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Hasty Touch + Inner Quiet + Innovation + Waste Not II Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, ~"+(int)Math.floor((current_craft.quality_goal-current_craft.craft_quality)*0.6)+" quality from the goal.");

		List<Skill> quality_rotation10 = new ArrayList<Skill>();
		Craft c12 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation10;
		current_craft = c12;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not");
		s.useSkill(current_craft);
		current_rotation.add(s);
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			if (current_craft.BuffList.get("Waste Not").stackCount==0) {
				s = SKILLLIST.get("Waste Not");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Hasty Touch + Inner Quiet + Innovation + Waste Not Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, ~"+(int)Math.floor((current_craft.quality_goal-current_craft.craft_quality)*0.6)+" quality from the goal.");

		List<Skill> quality_rotation11 = new ArrayList<Skill>();
		Craft c13 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation11;
		current_craft = c13;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not");
		s.useSkill(current_craft);
		current_rotation.add(s);
		if (current_craft.BuffList.get("Innovation").stackCount==0) {
			s = SKILLLIST.get("Innovation");
			s.useSkill(current_craft);
			current_rotation.add(s);
		}
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Waste Not").stackCount==0) {
				s = SKILLLIST.get("Waste Not");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Hasty Touch + Inner Quiet + Innovation (Once) + Waste Not Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, ~"+(int)Math.floor((current_craft.quality_goal-current_craft.craft_quality)*0.6)+" quality from the goal.");

		List<Skill> quality_rotation12 = new ArrayList<Skill>();
		Craft c14 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		current_rotation = quality_rotation12;
		current_craft = c14;
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(current_craft);
		current_rotation.add(s);
		s = SKILLLIST.get("Waste Not II");
		s.useSkill(current_craft);
		current_rotation.add(s);
		if (current_craft.BuffList.get("Innovation").stackCount==0) {
			s = SKILLLIST.get("Innovation");
			s.useSkill(current_craft);
			current_rotation.add(s);
		}
		while (current_craft.craft_quality<current_craft.quality_goal && IsThereEnoughTurns(current_craft.craft_durability,current_craft.BuffList,PROGRESS_FINAL_STEPS)) {
			if (current_craft.BuffList.get("Waste Not").stackCount==0) {
				s = SKILLLIST.get("Waste Not II");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo = AttemptMastersMend(PROGRESS_FINAL_STEPS, current_rotation, current_craft, combo);
		}
		System.out.println("Hasty Touch + Inner Quiet + Innovation (Once) + Waste Not II Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, ~"+(int)Math.floor((current_craft.quality_goal-current_craft.craft_quality)*0.6)+" quality from the goal.");

	}

	private static void RunCraftCheck(String[] preAbilities, String[] repeatAbilities, String[] reapplyBuffs, int qualityCPRemaining, int PROGRESS_FINAL_STEPS) {
		List<Skill> rotation = new ArrayList<Skill>();
		Craft craft = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		for (String s : preAbilities) {
			Skill ss = SKILLLIST.get(s);
			ss.useSkill(craft);
			rotation.add(ss);
		}
		while (craft.craft_quality<craft.quality_goal && IsThereEnoughTurns(craft.craft_durability,craft.BuffList,PROGRESS_FINAL_STEPS)) {
			for (String s : reapplyBuffs) {
				if (craft.BuffList.get(s.replaceAll(Pattern.quote("Waste Not II"),"Waste Not")).stackCount==0) {
					Skill ss = SKILLLIST.get(s);
					ss.useSkill(craft);
					rotation.add(ss);
				}
			}
		}
	}

	private static boolean AttemptMastersMend(int PROGRESS_FINAL_STEPS, List<Skill> quality_rotation1, Craft c3,
			boolean combo) {
		Skill s;
		if (!IsThereEnoughTurns(c3.craft_durability,c3.BuffList,PROGRESS_FINAL_STEPS)) {
			if (c3.craft_cp>=SKILLLIST.get("Master's Mend").CPCost) {
				s = SKILLLIST.get("Master's Mend");
				s.useSkill(c3);
				quality_rotation1.add(s);
				combo=!combo;
				System.out.println("Master's Mend is used.");
			}
		}
		return combo;
	}

}
