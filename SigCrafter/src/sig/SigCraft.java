package sig;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<Buff> BUFFLIST = new ArrayList<Buff>();
	public static List<Skill> SKILLLIST = new ArrayList<Skill>();
	
	public static int CRAFT_PROGRESS = 0;
	public static int CRAFT_QUALITY = 0;
	public static int CRAFT_DURABILITY = 0;
	public static int CRAFT_CP = 0;
	
	public static Craft BEST_CRAFT;
	
	//Quality = (0.37 * Control + 32.6) * (1 - 0.05 * min(max(Recipe Level - Character Level, 0), 5))
	//Good +50%, Excellent +300%
	
	//Fail conditions: Progress does not reach 100% when durability reaches 0
	
	public static ArrayList<Craft> SucceededCrafts = new ArrayList<Craft>();

	public static void main(String[] args) {
		SKILLLIST.add(new RapidSynthesis("Rapid Synthesis",0,false,9));
		SKILLLIST.add(new BasicSynthesis("Basic Synthesis",0,true,1));
		SKILLLIST.add(new BrandOfTheElements("Brand of the Elements",6,true,37));
		SKILLLIST.add(new BasicTouch("Basic Touch",18,true,5));
		SKILLLIST.add(new HastyTouch("Hasty Touch",0,false,9));
		SKILLLIST.add(new StandardTouch("Standard Touch",32,true,18));
		//SKILLLIST.add(new Skill("Byregot's Blessing",24,true,50)); //TODO We don't know how this works yet.
		SKILLLIST.add(new TricksOfTheTrade("Tricks of the Trade",0,true,13));
		SKILLLIST.add(new MastersMend("Master's Mend",88,true,7));
		SKILLLIST.add(new WasteNot("Waste Not",56,true,15));
		SKILLLIST.add(new WasteNotII("Waste Not II",98,true,47));
		SKILLLIST.add(new InnerQuiet("Inner Quiet",18,true,11));
		SKILLLIST.add(new Veneration("Veneration",18,true,15));
		SKILLLIST.add(new GreatStrides("Great Strides",32,true,21));
		SKILLLIST.add(new Innovation("Innovation",18,true,26));
		SKILLLIST.add(new NameOfTheElements("Name of the Elements",30,true,37));
		SKILLLIST.add(new Observe("Observe",7,true,13));
		
		BUFFLIST.add(new Buff("Inner Quiet",0));
		BUFFLIST.add(new Buff("Veneration",0));
		BUFFLIST.add(new Buff("Great Strides",0));
		BUFFLIST.add(new Buff("Innovation",0));
		BUFFLIST.add(new Buff("Name of the Elements",0));
		BUFFLIST.add(new Buff("Name of the Elements Has Been Used",0));
		BUFFLIST.add(new Buff("Inner Quiet", 0));
		BUFFLIST.add(new Buff("Waste Not",0));
		BUFFLIST.add(new Buff("Waste Not II",0));
		
		SetupCraft();
	}
	
	public static void SetupCraft() {
		List<Skill> skills = new ArrayList<Skill>();
		skills.addAll(SKILLLIST);
		skills.removeIf((skill)->{return skill.lvReq>LEVEL;});
		for (Skill s : skills) {
			Craft c = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,CP,1,1,1,RECIPE_LEVEL,Status.NORMAL);
			for (Buff b : BUFFLIST) {
				c.BuffList.put(b.name,new Buff(b.name,b.stackCount));
			}
			if (s.canBeUsed(c)) {s.useSkill(c);}
			if (c.craftSucceeded()) {SucceededCrafts.add(c);}
			System.out.println(c);
		}
		System.out.println(SucceededCrafts);
	}

}
