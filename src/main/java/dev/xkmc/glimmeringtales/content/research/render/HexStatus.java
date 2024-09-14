package dev.xkmc.glimmeringtales.content.research.render;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.ChatFormatting;

import java.util.Locale;

public class HexStatus {

	public enum Save implements GTLang.EnumLang {
		YES(ChatFormatting.GREEN), NO(ChatFormatting.RED);

		public final ChatFormatting col;

		Save(ChatFormatting col) {
			this.col = col;
		}

		public String defText() {
			return this == YES ? "Saved" : "Not Saved";
		}

		public int getColor() {
			return col.getColor();
		}

	}

	public enum Compile implements GTLang.EnumLang {
		COMPLETE(ChatFormatting.GREEN),
		FAILED(ChatFormatting.DARK_PURPLE),
		EDITING(ChatFormatting.BLUE),
		ERROR(ChatFormatting.RED);

		public final ChatFormatting col;

		Compile(ChatFormatting col) {
			this.col = col;
		}

		public int getColor() {
			return col.getColor();
		}

		@Override
		public String defText() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

}
