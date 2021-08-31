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
			System.out.println("\tUsing Veneration rotation for Progress. Quality CP Available: "+qualityCPRemaining);
		} else {
			PROGRESS_ROTATION.addAll(progress_rotation1);
			qualityCPRemaining = c1.craft_cp;
			System.out.println("\tUsing standard rotation for Progress. Quality CP Available: "+qualityCPRemaining);
		}

		//Can we just straight up craft?
		List<Skill> quality_rotation1 = new ArrayList<Skill>();
		Craft c3 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		boolean combo=false;
		while (c3.craft_quality<c3.quality_goal && c3.craft_durability>progressSteps*10) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(c3);
			quality_rotation1.add(s);
			combo=!combo;
		}
		System.out.println("Raw Quality Rotation leaves "+c3.craft_cp+" CP, "+c3.craft_durability+" durability, "+(c3.quality_goal-c3.craft_quality)+" quality from the goal.");
		
		//Add Inner Quiet
		List<Skill> quality_rotation2 = new ArrayList<Skill>();
		Craft c4 = new Craft(CONTROL,LEVEL,CP,BASE_PROGRESS,PROGRESS_GOAL,QUALITY_GOAL,GUARANTEED,DURABILITY,CRAFT_PROGRESS,CRAFT_QUALITY,DURABILITY,qualityCPRemaining,1,1,1,RECIPE_LEVEL,Status.NORMAL,BUFFLIST);
		s = SKILLLIST.get("Inner Quiet");
		s.useSkill(c4);
		quality_rotation2.add(s);
		combo=false;
		while (c4.craft_quality<c4.quality_goal && c4.craft_durability>progressSteps*10) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(c4);
			quality_rotation2.add(s);
			combo=!combo;
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
		}
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
		}
		System.out.println("Raw Quality + Inner Quiet + Innovation + Waste Not Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");
		
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
		}
		System.out.println("Raw Quality + Inner Quiet + Waste Not Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");

		
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
		}
		System.out.println("Raw Quality + Inner Quiet + Innovation + Waste Not II Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");
		
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			s = !combo?SKILLLIST.get("Basic Touch"):SKILLLIST.get("Standard Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
			combo=!combo;
		}
		System.out.println("Raw Quality + Inner Quiet + Waste Not II Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, "+(current_craft.quality_goal-current_craft.craft_quality)+" quality from the goal.");
				
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Waste Not II").stackCount==0) {
				s = SKILLLIST.get("Waste Not II");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Innovation").stackCount==0) {
				s = SKILLLIST.get("Innovation");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			if (current_craft.BuffList.get("Waste Not II").stackCount==0) {
				s = SKILLLIST.get("Waste Not II");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Waste Not").stackCount==0) {
				s = SKILLLIST.get("Waste Not");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
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
		while (current_craft.craft_quality<current_craft.quality_goal && current_craft.craft_durability>progressSteps*10) {
			if (current_craft.BuffList.get("Waste Not II").stackCount==0) {
				s = SKILLLIST.get("Waste Not II");
				s.useSkill(current_craft);
				current_rotation.add(s);
			}
			s = SKILLLIST.get("Hasty Touch");
			s.useSkill(current_craft);
			current_rotation.add(s);
		}
		System.out.println("Hasty Touch + Inner Quiet + Innovation (Once) + Waste Not II Rotation leaves "+current_craft.craft_cp+" CP, "+current_craft.craft_durability+" durability, ~"+(int)Math.floor((current_craft.quality_goal-current_craft.craft_quality)*0.6)+" quality from the goal.");

	}

}
