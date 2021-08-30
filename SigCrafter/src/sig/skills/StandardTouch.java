package sig.skills;

import sig.Craft;
import sig.Skill;

public class StandardTouch extends Skill {

	public StandardTouch(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		if (c.SkillList.size()>0&&c.SkillList.get(c.SkillList.size()-1).name.equalsIgnoreCase("Basic Touch")) {
			CPCost = 18;
		} else {
			CPCost = 32;
		}
		super.useSkill(c);
		c.craft_quality += 1.25 * c.quality_mult * (0.37*c.control+32.6)*(1-0.05*Math.min(Math.max(c.recipe_level-c.level,0),5));
		c.craft_durability -= 10 * c.durability_mult;
		if (c.BuffList.get("Inner Quiet").stackCount>0) {c.BuffList.get("Inner Quiet").stackCount++;}
	}

}
