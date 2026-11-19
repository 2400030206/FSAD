package com.ai.startup.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class AIService {

private static final Pattern SENTENCE_SPLIT=Pattern.compile("[.!?]+\\s*");

private static class LanguagePack{
	private final String summary;
	private final String highlights;
	private final String quiz;
	private final String pointWise;
	private final String answerExplanation;
	private final String revision;
	private final String chapterSplit;
	private final String voiceScript;
	private final String videoScript;
	private final String languageCode;

	private LanguagePack(String summary,String highlights,String quiz,String pointWise,String answerExplanation,
						 String revision,String chapterSplit,String voiceScript,String videoScript){
		this(summary,highlights,quiz,pointWise,answerExplanation,revision,chapterSplit,voiceScript,videoScript,"en");
	}

	private LanguagePack(String summary,String highlights,String quiz,String pointWise,String answerExplanation,
						 String revision,String chapterSplit,String voiceScript,String videoScript,String languageCode){
		this.summary=summary;
		this.highlights=highlights;
		this.quiz=quiz;
		this.pointWise=pointWise;
		this.answerExplanation=answerExplanation;
		this.revision=revision;
		this.chapterSplit=chapterSplit;
		this.voiceScript=voiceScript;
		this.videoScript=videoScript;
		this.languageCode=languageCode;
	}

	public String getLanguageCode(){
		return languageCode;
	}

	public static LanguagePack from(String language){
		if(language == null) return english();
		String lang=language.trim().toLowerCase(Locale.ROOT);
		if(lang.equals("hindi")) return hindi();
		if(lang.equals("telugu")) return telugu();
		return english();
	}

	private static LanguagePack english(){
		return new LanguagePack(
			"Summary",
			"Important Line Highlight",
			"Quiz",
			"Point-wise Explanation",
			"Answer Explanation",
			"5-Minute Revision Mode",
			"Chapter / Section Split",
			"Voice Read-Out Script",
			"AI Video Script",
			"en"
		);
	}

	private static LanguagePack hindi(){
		return new LanguagePack(
			"सारांश",
			"महत्वपूर्ण पंक्ति हाइलाइट",
			"प्रश्नोत्तरी",
			"पॉइंट-वाइज़ व्याख्या",
			"उत्तर व्याख्या",
			"5-मिनट पुनरावृत्ति मोड",
			"अध्याय / अनुभाग विभाजन",
			"वॉइस रीड-आउट स्क्रिप्ट",
			"एआई वीडियो स्क्रिप्ट",
			"hi"
		);
	}

	private static LanguagePack telugu(){
		return new LanguagePack(
			"సారాంశం",
			"ముఖ్యమైన లైన్ హైలైట్",
			"క్విజ్",
			"పాయింట్-వైజ్ వివరణ",
			"సమాధానం వివరణ",
			"5-నిమిషాల రివిజన్ మోడ్",
			"అధ్యాయం / విభాగ విభజన",
			"వాయిస్ రీడ్-అవుట్ స్క్రిప్ట్",
			"ఎఐ వీడియో స్క్రిప్ట్",
			"te"
		);
	}
}

public String explainText(String text,String level,String language){

String langInstruction="";

if(language.equals("telugu"))
langInstruction="Explain in Telugu + English.";

else if(language.equals("hindi"))
langInstruction="Explain in Hindi + English.";

else
langInstruction="Explain fully in English.";

if(level.equals("simple"))
return langInstruction + "\nExplain in very simple school-level language:\n"+text;

if(level.equals("exam"))
return langInstruction + "\nExplain focusing exam important points:\n"+text;

return langInstruction + "\nAdvanced detailed explanation:\n"+text;
}

public List<String> highlightPoints(String text){

return List.of(
"🔴 Important definition example",
"⭐ Exam key point example"
);
}

public String quickRevision(String text){
return buildRevision(text,Level.SIMPLE);
}

public String generateStudyResponse(String text,String level,String language,
									List<String> outputs,List<com.ai.startup.model.Question> quizQuestions){
	String cleaned=normalizeText(text);
	List<String> sentences=extractSentences(cleaned);
	OutputSelection selection=OutputSelection.from(outputs);
	if(selection.isEmpty()){
		return "No output selected.";
	}

	Level lvl=Level.from(level);
	LanguagePack pack=LanguagePack.from(language);

	StringBuilder sb=new StringBuilder();

	if(selection.summary){
		appendHeading(sb,pack.summary);
		sb.append(buildSummary(sentences,lvl));
		sb.append("\n\n");
	}

	if(selection.highlights){
		appendHeading(sb,pack.highlights);
		sb.append(buildHighlights(sentences,lvl));
		sb.append("\n\n");
	}

	if(selection.quiz){
		appendHeading(sb,pack.quiz);
		sb.append(buildQuizSection(quizQuestions));
		sb.append("\n\n");
	}

	if(selection.pointWise){
		appendHeading(sb,pack.pointWise);
		sb.append(buildPointWise(cleaned,lvl,pack));
		sb.append("\n\n");
	}

	if(selection.answerExplanation){
		appendHeading(sb,pack.answerExplanation);
		sb.append(buildAnswerExplanation(quizQuestions));
		sb.append("\n\n");
	}

	if(selection.revision){
		appendHeading(sb,pack.revision);
		sb.append(buildRevision(cleaned,lvl));
		sb.append("\n\n");
	}

	if(selection.chapterSplit){
		appendHeading(sb,pack.chapterSplit);
		sb.append(buildChapterSplit(cleaned,lvl));
		sb.append("\n\n");
	}

	if(selection.voiceScript){
		appendHeading(sb,pack.voiceScript);
		sb.append(buildVoiceScript(sentences,lvl));
		sb.append("\n\n");
	}

	if(selection.videoScript){
		appendHeading(sb,pack.videoScript);
		sb.append(buildVideoScript(sentences,lvl));
		sb.append("\n\n");
	}

	return sb.toString().trim();
}

private static void appendHeading(StringBuilder sb,String heading){
	sb.append("=== ").append(heading).append(" ===\n");
}

private enum Level{
	SIMPLE,
	EXAM,
	ADVANCED;

	public static Level from(String level){
		if(level == null) return SIMPLE;
		String normalized=level.trim().toLowerCase(Locale.ROOT);
		if(normalized.equals("exam")) return EXAM;
		if(normalized.equals("advanced")) return ADVANCED;
		return SIMPLE;
	}
}

private static class OutputSelection{
	boolean summary;
	boolean highlights;
	boolean quiz;
	boolean pointWise;
	boolean answerExplanation;
	boolean revision;
	boolean chapterSplit;
	boolean voiceScript;
	boolean videoScript;

	boolean isEmpty(){
		return !(summary || highlights || quiz || pointWise || answerExplanation || revision || chapterSplit || voiceScript || videoScript);
	}

	static OutputSelection from(List<String> outputs){
		OutputSelection selection=new OutputSelection();
		if(outputs == null) return selection;
		for(String raw:outputs){
			if(raw == null) continue;
			String value=raw.trim().toLowerCase(Locale.ROOT);
			if(value.isEmpty()) continue;
			if(value.equals("summary")) selection.summary=true;
			else if(value.equals("quiz")) selection.quiz=true;
			else if(value.equals("pointwise") || value.equals("point-wise") || value.equals("point_wise")) selection.pointWise=true;
			else if(value.equals("highlights") || value.equals("highlight") || value.equals("important_line_highlight")) selection.highlights=true;
			else if(value.equals("revision")) selection.revision=true;
			else if(value.equals("video") || value.equals("ai_video_script")) selection.videoScript=true;
			else if(value.equals("answer_explanation")) selection.answerExplanation=true;
			else if(value.equals("chapter_split") || value.equals("chapter")) selection.chapterSplit=true;
			else if(value.equals("voice") || value.equals("voice_script") || value.equals("voice_read_out")) selection.voiceScript=true;
		}
		return selection;
	}
}

private String buildSummary(List<String> sentences,Level level){
	List<String> picks=pickSentences(sentences,level == Level.SIMPLE ? 3 : 5);
	if(picks.isEmpty()){
		return "No readable content found in the PDF text.";
	}

	StringBuilder sb=new StringBuilder();
	int index=1;
	for(String s:picks){
		if(level == Level.EXAM){
			sb.append("⭐ ");
		}
		sb.append(index++).append(") ").append(s.trim()).append("\n");
	}
	return sb.toString().trim();
}

private String buildHighlights(List<String> sentences,Level level){
	List<String> important=extractImportantLines(sentences,5);
	if(important.isEmpty()){
		return "🔴 Important Line: No strong definition or conclusion found in the text.";
	}
	StringBuilder sb=new StringBuilder();
	for(int i=0;i<important.size();i++){
		String line=important.get(i);
		if(i % 2 == 0){
			sb.append("🔴 Important Line: ").append(line).append("\n");
		}else{
			sb.append("⭐ Exam Important Point: ").append(line).append("\n");
		}
	}
	return sb.toString().trim();
}

private String buildQuizSection(List<com.ai.startup.model.Question> questions){
	if(questions == null || questions.isEmpty()){
		return "No quiz questions could be generated from the PDF text.";
	}
	StringBuilder sb=new StringBuilder();
	int i=1;
	for(com.ai.startup.model.Question q:questions){
		sb.append(i++).append(") ").append(q.getQuestion()).append("\n");
	}
	return sb.toString().trim();
}

private String buildAnswerExplanation(List<com.ai.startup.model.Question> questions){
	if(questions == null || questions.isEmpty()){
		return "No answer explanations available because no questions were generated.";
	}
	StringBuilder sb=new StringBuilder();
	int i=1;
	for(com.ai.startup.model.Question q:questions){
		sb.append(i++).append(") ").append(q.getQuestion()).append("\n");
		sb.append("Correct Answer: ").append(nullToFallback(q.getCorrectAnswer(),"Refer to the PDF text.")).append("\n");
		sb.append("Why correct: The answer is taken directly from the PDF text.\n");
		if("MCQ".equalsIgnoreCase(q.getType())){
			sb.append("Why other options are wrong: The PDF does not support those options.\n");
		}
		sb.append("\n");
	}
	return sb.toString().trim();
}

private String buildRevision(String text,Level level){
	List<String> sentences=extractSentences(text);
	List<String> summary=pickSentences(sentences,8);
	StringBuilder sb=new StringBuilder();
	sb.append("Ultra-short summary (max 8 points):\n");
	int i=1;
	for(String s:summary){
		sb.append(i++).append(") ").append(s).append("\n");
	}
	sb.append("\n5 key exam questions:\n");
	List<String> questions=pickSentences(sentences,5);
	int q=1;
	for(String sentence:questions){
		sb.append(q++).append(") Write a short note on: ").append(sentence).append("\n");
	}
	sb.append("\nKey terms:\n");
	List<String> terms=extractKeyTerms(text,10);
	if(terms.isEmpty()){
		sb.append("No clear key terms found.");
	}else{
		sb.append(String.join(", ",terms));
	}
	return sb.toString().trim();
}

private String buildChapterSplit(String text,Level level){
	List<String> sections=splitIntoSections(text);
	StringBuilder sb=new StringBuilder();
	int i=1;
	for(String section:sections){
		List<String> sentences=extractSentences(section);
		sb.append("Section ").append(i++).append(":\n");
		sb.append("Short summary: ").append(firstOrFallback(sentences,"No content.")).append("\n");
		sb.append("Important points: ").append(String.join("; ",pickSentences(sentences,3))).append("\n");
		sb.append("Quiz: ");
		List<String> quiz=pickSentences(sentences,2);
		if(quiz.isEmpty()){
			sb.append("No quiz items available.\n\n");
		}else{
			sb.append("1) ").append(quiz.get(0));
			if(quiz.size() > 1){
				sb.append(" 2) ").append(quiz.get(1));
			}
			sb.append("\n\n");
		}
	}
	return sb.toString().trim();
}

private String buildVoiceScript(List<String> sentences,Level level){
	List<String> picks=pickSentences(sentences,5);
	if(picks.isEmpty()){
		return "No readable content found for voice read-out.";
	}
	StringBuilder sb=new StringBuilder();
	for(String s:picks){
		sb.append(s.trim()).append(". ");
	}
	return sb.toString().trim();
}

private String buildVideoScript(List<String> sentences,Level level){
	List<String> picks=pickSentences(sentences,6);
	String intro=firstOrFallback(picks,"This video explains the main ideas from the PDF.");
	String concept=picks.size() > 1 ? picks.get(1) : intro;
	String example=findExampleSentence(sentences);
	if(example == null){
		example=concept;
	}
	String recap=picks.size() > 2 ? picks.get(2) : concept;

	StringBuilder sb=new StringBuilder();
	sb.append("Scene 1: Introduction\n");
	sb.append(intro).append("\n\n");
	sb.append("Scene 2: Concept Explanation\n");
	sb.append(concept).append("\n\n");
	sb.append("Scene 3: Example\n");
	sb.append(example).append("\n\n");
	sb.append("Scene 4: Quick Recap\n");
	sb.append(recap).append("\n");
	return sb.toString().trim();
}

private String buildPointWise(String text,Level level,LanguagePack pack){
	List<String> sentences=extractSentences(text);
	if(sentences.isEmpty()){
		return "No readable content found in the PDF text.";
	}

	StringBuilder sb=new StringBuilder();
	int index=1;
	for(String sentence:sentences){
		sb.append(index++).append(") ");
		if(pack.getLanguageCode().equals("hi")){
			sb.append("बिंदु: ");
		}else if(pack.getLanguageCode().equals("te")){
			sb.append("పాయింట్: ");
		}
		sb.append(sentence.trim()).append("\n");

		if(level == Level.EXAM){
			List<String> terms=extractKeyTerms(sentence,5);
			if(!terms.isEmpty()){
				if(pack.getLanguageCode().equals("hi")){
					sb.append("कीवर्ड्स: ");
				}else if(pack.getLanguageCode().equals("te")){
					sb.append("కీవర్డ్స్: ");
				}else{
					sb.append("Keywords: ");
				}
				sb.append(String.join(", ",terms)).append("\n");
			}
		}
		sb.append("\n");
	}

	return sb.toString().trim();
}

private static String normalizeText(String text){
	if(text == null) return "";
	return text.replace("\r"," ").replace("\n"," ").trim();
}

private static List<String> extractSentences(String text){
	if(text == null || text.trim().isEmpty()) return Collections.emptyList();
	String[] parts=SENTENCE_SPLIT.split(text.trim());
	List<String> sentences=new ArrayList<>();
	for(String part:parts){
		String trimmed=part.trim();
		if(trimmed.length() < 10) continue;
		sentences.add(trimmed);
	}
	return sentences;
}

private static List<String> pickSentences(List<String> sentences,int max){
	if(sentences == null || sentences.isEmpty()) return Collections.emptyList();
	List<String> picked=new ArrayList<>();
	for(String s:sentences){
		if(picked.size() >= max) break;
		picked.add(s);
	}
	return picked;
}

private static List<String> extractImportantLines(List<String> sentences,int max){
	List<String> important=new ArrayList<>();
	for(String s:sentences){
		String lower=s.toLowerCase(Locale.ROOT);
		if(lower.contains(" is ") || lower.contains(" are ") || lower.contains(" means ")
			|| lower.contains(" therefore ") || lower.contains(" because ") || lower.contains(" defined ")){
			important.add(s);
			if(important.size() >= max) break;
		}
	}
	if(important.isEmpty()){
		important.addAll(pickSentences(sentences,Math.min(max,3)));
	}
	return important;
}

private static List<String> extractKeyTerms(String text,int max){
	if(text == null || text.trim().isEmpty()) return Collections.emptyList();
	String[] tokens=text.replaceAll("[^A-Za-z0-9 ]"," ").split("\\s+");
	Set<String> terms=new HashSet<>();
	for(String token:tokens){
		if(terms.size() >= max) break;
		String cleaned=token.trim();
		if(cleaned.length() < 5) continue;
		terms.add(cleaned);
	}
	return new ArrayList<>(terms);
}

private static List<String> splitIntoSections(String text){
	if(text == null || text.trim().isEmpty()) return Collections.emptyList();
	String[] lines=text.split("\\n");
	List<String> sections=new ArrayList<>();
	StringBuilder current=new StringBuilder();
	for(String line:lines){
		String trimmed=line.trim();
		boolean heading=trimmed.matches("(Chapter|CHAPTER|Section|SECTION).*") || (trimmed.length() > 0 && trimmed.length() < 40 && trimmed.equals(trimmed.toUpperCase(Locale.ROOT)));
		if(heading && current.length() > 0){
			sections.add(current.toString());
			current.setLength(0);
		}
		if(!trimmed.isEmpty()){
			current.append(trimmed).append(" ");
		}
	}
	if(current.length() > 0){
		sections.add(current.toString());
	}
	if(sections.size() <= 1){
		List<String> sentences=extractSentences(text);
		if(sentences.size() <= 3){
			return sections;
		}
		sections.clear();
		int chunk=Math.max(1,sentences.size()/3);
		for(int i=0;i<sentences.size();i+=chunk){
			StringBuilder sb=new StringBuilder();
			for(int j=i;j<Math.min(i+chunk,sentences.size());j++){
				sb.append(sentences.get(j)).append(" ");
			}
			sections.add(sb.toString().trim());
		}
	}
	return sections;
}

private static String firstOrFallback(List<String> sentences,String fallback){
	if(sentences == null || sentences.isEmpty()) return fallback;
	return sentences.get(0);
}

private static String nullToFallback(String value,String fallback){
	if(value == null || value.trim().isEmpty()) return fallback;
	return value;
}

private static String findExampleSentence(List<String> sentences){
	for(String s:sentences){
		String lower=s.toLowerCase(Locale.ROOT);
		if(lower.contains("for example") || lower.contains("e.g.") || lower.contains("such as")){
			return s;
		}
	}
	return null;
}

}
