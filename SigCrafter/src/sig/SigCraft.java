package sig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sig.skills.BasicSynthesis;
import sig.skills.BasicTouch;
import sig.skills.BrandOfTheElements;
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

public class SigCraft {
	public static int LEVEL = 48;
	public static int RECIPE_LEVEL = 45;
	public static int CP = 282;
	public static int BASE_PROGRESS = 51;
	public static int CONTROL = 185;
	public static int PROGRESS_GOAL = 158;
	public static int QUALITY_GOAL = 2309;
	public static boolean GUARANTEED = true;
	public static int DURABILITY = 80;
	
	public static Map<String,Buff> BUFFLIST = new HashMap<String,Buff>();
	public static Map<String,Skill> SKILLLIST = new HashMap<String,Skill>();
	
	public static int CRAFT_PROGRESS = 0;
	public static int CRAFT_QUALITY = 0;
	public static int CRAFT_DURABILITY = 0;
	public static int CRAFT_CP = 0;

	public static List<Skill> PROGRESS_ROTATION = new ArrayList<Skill>();
	
	public static Craft BEST_CRAFT;
	
	//Quality = (0.37 * Control + 32.6) * (1 - 0.05 * min(max(Recipe Level - Character Level, 0), 5))
	//Good +50%, Excellent +300%
	
	//Fail conditions: Progress does not reach 100% when durability reaches 0
	
	public static ArrayList<Craft> SucceededCrafts = new ArrayList<Craft>();

	public static void main(String[] args) {
		SKILLLIST.put("Rapid Synthesis",new RapidSynthesis("Rapid Synthesis",0,false,9));
		SKILLLIST.put("Basic Synthesis",new BasicSynthesis("Basic Synthesis",0,true,1));
		SKILLLIST.put("Brand of the Elements",new BrandOfTheElements("Brand of the Elements",6,true,37));
		SKILLLIST.put("Basic Touch",new BasicTouch("Basic Touch",18,true,5));
		SKILLLIST.put("Hasty Touch",new HastyTouch("Hasty Touch",0,false,9));
		SKILLLIST.put("Standard Touch",new StandardTouch("Standard Touch",32,true,18));
		//SKILLLIST.add(new Skill("Byregot's Blessing",24,true,50)); //TODO We don't know how this works yet.
		SKILLLIST.put("Tricks of the Trade",new TricksOfTheTrade("Tricks of the Trade",0,true,13));
		SKILLLIST.put("Master's Mend",new MastersMend("Master's Mend",88,true,7));
		SKILLLIST.put("Waste Not",new WasteNot("Waste Not",56,true,15));
		SKILLLIST.put("Waste Not II",new WasteNotII("Waste Not II",98,true,47));
		SKILLLIST.put("Inner Quiet",new InnerQuiet("Inner Quiet",18,true,11));
		SKILLLIST.put("Veneration",new Veneration("Veneration",18,true,15));
		SKILLLIST.put("Great Strides",new GreatStrides("Great Strides",32,true,21));
		SKILLLIST.put("Innovation",new Innovation("Innovation",18,true,26));
		SKILLLIST.put("Name of the Elements",new NameOfTheElements("Name of the Elements",30,true,37));
		SKILLLIST.put("Observe",new Observe("Observe",7,true,13));
		
		BUFFLIST.put("Inner Quiet",new Buff("Inner Quiet",0));
		BUFFLIST.put("Veneration",new Buff("Veneration",0));
		BUFFLIST.put("Great Strides",new Buff("Great Strides",0));
		BUFFLIST.put("Innovation",new Buff("Innovation",0));
		BUFFLIST.put("Name of the Elements",new Buff("Name of the Elements",0));
		BUFFLIST.put("Name of the Elements Has Been Used",new Buff("Name of the Elements Has Been Used",0));
		BUFFLIST.put("Inner Quiet",new Buff("Inner Quiet", 0));
		BUFFLIST.put("Waste Not",new Buff("Waste Not",0));
		BUFFLIST.put("Waste Not II",new Buff("Waste Not II",0));
		
		SetupCraft();
	}
	
	public static void SetupCraft() {

		List<Skill> progress_rotation1 = new ArrayList<Skill>();
		int progressSteps = 0;
		List<Skill> progress_rotation2 = new ArrayList<Skill>();

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
			System.out.println("\tUsing Veneration rotation for Progress.");
		} else {
			PROGRESS_ROTATION.addAll(progress_rotation1);
			System.out.println("\tUsing standard rotation for Progress.");
		}
	}

}
