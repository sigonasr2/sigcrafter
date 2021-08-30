package sig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Craft {
	public int control,base_control;
	public boolean guaranteed;
	public int level,recipe_level,cp,base_progress,progress_goal,quality_goal,durability;
	public int craft_progress,craft_quality,craft_durability,craft_cp;
	public Status craft_status;
	public double progress_mult,quality_mult,durability_mult;
	public List<Skill> SkillList = new ArrayList<Skill>();
	public Map<String,Buff> BuffList = new HashMap<String,Buff>();
	public List<Craft> CraftList = new ArrayList<Craft>();
	public Craft(int control, int level, int cp, int base_progress, int progress_goal, int quality_goal, boolean guaranteed,
			int durability, int craft_progress, int craft_quality, int craft_durability, int craft_cp,
			double progress_mult, double quality_mult, double durability_mult, int recipe_level,Status craft_status,Map<String,Buff> buffs) {
		this.control = control;
		this.base_control = control;
		this.level = level;
		this.cp = cp;
		this.base_progress = base_progress;
		this.progress_goal = progress_goal;
		this.quality_goal = quality_goal;
		this.guaranteed = guaranteed;
		this.durability = durability;
		this.craft_progress = craft_progress;
		this.craft_quality = craft_quality;
		this.craft_durability = craft_durability;
		this.craft_cp = craft_cp;
		this.progress_mult = progress_mult;
		this.quality_mult = quality_mult;
		this.durability_mult = durability_mult;
		this.recipe_level = recipe_level;
		this.craft_status = craft_status;
		for (Buff b : buffs.values()) {
			BuffList.put(b.getName(),new Buff(b.getName(),b.getStackCount()));
		}
	}
	public boolean craftFailed() {
		return craft_progress<progress_goal && durability<=0;
	}
	public boolean craftSucceeded() {
		return craft_progress>=progress_goal && craft_quality>=quality_goal;
	}
	@Override
	public String toString() {
		return "Craft [control=" + control + ", base_control=" + base_control + ", guaranteed=" + guaranteed
				+ ", level=" + level + ", recipe_level=" + recipe_level + ", cp=" + cp + ", base_progress="
				+ base_progress + ", progress_goal=" + progress_goal + ", quality_goal=" + quality_goal
				+ ", durability=" + durability + ", craft_progress=" + craft_progress + ", craft_quality="
				+ craft_quality + ", craft_durability=" + craft_durability + ", craft_cp=" + craft_cp
				+ ", craft_status=" + craft_status + ", progress_mult=" + progress_mult + ", quality_mult="
				+ quality_mult + ", durability_mult=" + durability_mult + ", SkillList=" + SkillList + ", BuffList="
				+ BuffList + ", CraftList=" + CraftList + "]";
	}
}
