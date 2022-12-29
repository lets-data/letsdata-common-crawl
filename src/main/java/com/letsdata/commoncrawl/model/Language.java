package com.letsdata.commoncrawl.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Language {
    AFAR(1,"aa","aar", "AFAR"),
    AFRIKAANS(2,"af","afr", "AFRIKAANS"),
    ALBANIAN(3,"sq","sqi", "ALBANIAN"),
    AMHARIC(4,"am","amh", "AMHARIC"),
    ARABIC(5,"ar","ara", "ARABIC"),
    ARMENIAN(6,"hy","hye", "ARMENIAN"),
    ASSAMESE(7,"as","asm", "ASSAMESE"),
    AZERBAIJANI(8,"az","aze", "AZERBAIJANI"),
    BASHKIR(9,"ba","bak", "BASHKIR"),
    BASQUE(10,"eu","eus", "BASQUE"),
    BELARUSIAN(11,"be","bel", "BELARUSIAN"),
    BENGALI(12,"bn","ben", "BENGALI"),
    BIHARI(13,"bh","bih", "BIHARI"),
    BOSNIAN(14,"bs","bos", "BOSNIAN"),
    BRETON(15,"br","bre", "BRETON"),
    BULGARIAN(16,"bg","bul", "BULGARIAN"),
    BURMESE(17,"my","mya", "BURMESE"),
    CATALAN(18,"ca","cat", "CATALAN"),
    CORSICAN(19,"co","cos", "CORSICAN"),
    CROATIAN(20,"hr","hrv", "CROATIAN"),
    CZECH(21,"cs","ces", "CZECH"),
    CHINESE(22,"zh","zho", "CHINESE"),
    DANISH(23,"da","dan", "DANISH"),
    DUTCH(24,"nl","nld", "DUTCH"),
    ENGLISH(25,"en","eng", "ENGLISH"),
    ESPERANTO(26,"eo","epo", "ESPERANTO"),
    ESTONIAN(27,"et","est", "ESTONIAN"),
    FAROESE(28,"fo","fao", "FAROESE"),
    FIJIAN(29,"fj","fij", "FIJIAN"),
    FINNISH(30,"fi","fin", "FINNISH"),
    FRENCH(31,"fr","fra", "FRENCH"),
    FRISIAN(32,"fy","fry", "FRISIAN"),
    GALICIAN(33,"gl","glg", "GALICIAN"),
    GANDA(34,"lg","lug", "GANDA"),
    GEORGIAN(35,"ka","kat", "GEORGIAN"),
    GERMAN(36,"de","deu", "GERMAN"),
    GREEK(37,"el","ell", "GREEK"),
    GREENLANDIC(38,"kl","kal", "GREENLANDIC"),
    GUARANI(39,"gn","grn", "GUARANI"),
    GUJARATI(40,"gu","guj", "GUJARATI"),
    HAITIAN_CREOLE(41,"ht","hat", "HAITIAN_CREOLE"),
    HAUSA(42,"ha","hau", "HAUSA"),
    HAWAIIAN(43,"haw","haw", "HAWAIIAN"),
    HEBREW(44,"iw","heb", "HEBREW"),
    HINDI(45,"hi","hin", "HINDI"),
    HUNGARIAN(46,"hu","hun", "HUNGARIAN"),
    ICELANDIC(47,"is","isl", "ICELANDIC"),
    INDONESIAN(48,"id","ind", "INDONESIAN"),
    INTERLINGUA(49,"ia","ina", "INTERLINGUA"),
    INTERLINGUE(50,"ie","ile", "INTERLINGUE"),
    INUPIAK(51,"ik","ipk", "INUPIAK"),
    IRISH(52,"ga","gle", "IRISH"),
    ITALIAN(53,"it","ita", "ITALIAN"),
    JAVANESE(54,"jw","jav", "JAVANESE"),
    JAPANESE(55,"ja","jpn", "JAPANESE"),
    KANNADA(56,"kn","kan", "KANNADA"),
    KAZAKH(57,"kk","kaz", "KAZAKH"),
    KHASI(58,"kha","kha", "KHASI"),
    KHMER(59,"km","khm", "KHMER"),
    KINYARWANDA(60,"rw","kin", "KINYARWANDA"),
    KURDISH(61,"ku","kur", "KURDISH"),
    KYRGYZ(62,"ky","kir", "KYRGYZ"),
    KOREAN(63,"ko","kor", "KOREAN"),
    LAOTHIAN(64,"lo","lao", "LAOTHIAN"),
    LATIN(65,"la","lat", "LATIN"),
    LATVIAN(66,"lv","lav", "LATVIAN"),
    LINGALA(67,"ln","lin", "LINGALA"),
    LITHUANIAN(68,"lt","lit", "LITHUANIAN"),
    LUXEMBOURGISH(69,"lb","ltz", "LUXEMBOURGISH"),
    MACEDONIAN(70,"mk","mkd", "MACEDONIAN"),
    MALAGASY(71,"mg","mlg", "MALAGASY"),
    MALAY(72,"ms","msa", "MALAY"),
    MALAYALAM(73,"ml","mal", "MALAYALAM"),
    MALTESE(74,"mt","mlt", "MALTESE"),
    MANX(75,"gv","glv", "MANX"),
    MAORI(76,"mi","mri", "MAORI"),
    MARATHI(77,"mr","mar", "MARATHI"),
    MAURITIAN_CREOLE(78,"mfe","mfe", "MAURITIAN_CREOLE"),
    MONGOLIAN(79,"mn","mon", "MONGOLIAN"),
    NAURU(80,"na","nau", "NAURU"),
    NEPALI(81,"ne","nep", "NEPALI"),
    NORWEGIAN(82,"no","nor", "NORWEGIAN"),
    NORWEGIAN_N(83,"nn","nno", "NORWEGIAN_N"),
    NYANJA(84,"ny","nya", "NYANJA"),
    OCCITAN(85,"oc","oci", "OCCITAN"),
    ORIYA(86,"or","ori", "ORIYA"),
    OROMO(87,"om","orm", "OROMO"),
    PASHTO(88,"ps","pus", "PASHTO"),
    PEDI(89,"nso","nso", "PEDI"),
    PERSIAN(90,"fa","fas", "PERSIAN"),
    POLISH(91,"pl","pol", "POLISH"),
    PORTUGUESE(92,"pt","por", "PORTUGUESE"),
    PUNJABI(93,"pa","pan", "PUNJABI"),
    QUECHUA(94,"qu","que", "QUECHUA"),
    RHAETO_ROMANCE(95,"rm","roh", "RHAETO_ROMANCE"),
    ROMANIAN(96,"ro","ron", "ROMANIAN"),
    RUNDI(97,"rn","run", "RUNDI"),
    RUSSIAN(98,"ru","rus", "RUSSIAN"),
    SAMOAN(99,"sm","smo", "SAMOAN"),
    SANSKRIT(100,"sa","san", "SANSKRIT"),
    SCOTS(101,"sco","sco", "SCOTS"),
    SCOTS_GAELIC(102,"gd","gla", "SCOTS_GAELIC"),
    SERBIAN(103,"sr","srp", "SERBIAN"),
    SESELWA(104,"crs","crs", "SESELWA"),
    SESOTHO(105,"st","sot", "SESOTHO"),
    SHONA(106,"sn","sna", "SHONA"),
    SINHALESE(107,"si","sin", "SINHALESE"),
    SLOVAK(108,"sk","slk", "SLOVAK"),
    SLOVENIAN(109,"sl","slv", "SLOVENIAN"),
    SOMALI(110,"so","som", "SOMALI"),
    SPANISH(111,"es","spa", "SPANISH"),
    SUNDANESE(112,"su","sun", "SUNDANESE"),
    SWAHILI(113,"sw","swa", "SWAHILI"),
    SWEDISH(114,"sv","swe", "SWEDISH"),
    SYRIAC(115,"syr","syr", "SYRIAC"),
    TAGALOG(116,"tl","tgl", "TAGALOG"),
    TAJIK(117,"tg","tgk", "TAJIK"),
    TAMIL(118,"ta","tam", "TAMIL"),
    TATAR(119,"tt","tat", "TATAR"),
    TELUGU(120,"te","tel", "TELUGU"),
    THAI(121,"th","tha", "THAI"),
    TONGA(122,"to","ton", "TONGA"),
    TSONGA(123,"ts","tso", "TSONGA"),
    TSWANA(124,"tn","tsn", "TSWANA"),
    TURKISH(125,"tr","tur", "TURKISH"),
    TURKMEN(126,"tk","tuk", "TURKMEN"),
    UIGHUR(127,"ug","uig", "UIGHUR"),
    UKRAINIAN(128,"uk","ukr", "UKRAINIAN"),
    URDU(129,"ur","urd", "URDU"),
    UZBEK(130,"uz","uzb", "UZBEK"),
    VIETNAMESE(131,"vi","vie", "VIETNAMESE"),
    VOLAPUK(132,"vo","vol", "VOLAPUK"),
    WARAY_PHILIPPINES(133,"war","war", "WARAY_PHILIPPINES"),
    WELSH(134,"cy","cym", "WELSH"),
    WOLOF(135,"wo","wol", "WOLOF"),
    XHOSA(136,"xh","xho", "XHOSA"),
    YIDDISH(137,"yi","yid", "YIDDISH"),
    YORUBA(138,"yo","yor", "YORUBA"),
    ZULU(139,"zu","zul", "ZULU");

    private final int langId;
    private final String code;
    private final String isoCode;
    private final String name;

    private Language(int id, String code, String isoCode, String name) {
        this.langId = id;
        this.code = code;
        this.isoCode = isoCode;
        this.name = name;
    }

    public int getLangId() {
        return langId;
    }

    public String getCode() {
        return code;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Language{");
        sb.append("langId=").append(langId);
        sb.append(", code='").append(code).append('\'');
        sb.append(", isoCode='").append(isoCode).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    private static Map<String, Language> codeLanguageMap = null;
    public static Language fromCode(String code) {
        if (codeLanguageMap == null) {
            synchronized (Language.class) {
                if (codeLanguageMap == null) {
                    Map<String, Language> lookupMap = new HashMap<>();
                    for (Language language : Language.values()) {
                        if (lookupMap.containsKey(language.code)) {
                            throw new RuntimeException("duplicate language in the lookup map - code: "+language.code+", name: "+language.name);
                        }

                        lookupMap.put(language.code, language);
                    }
                    codeLanguageMap = Collections.unmodifiableMap(lookupMap);
                }
            }
        }

        return codeLanguageMap.get(code);
    }

    private static Map<String, Language> isoCodeLanguageMap = null;
    public static Language fromISOCode(String isoCode) {
        if (isoCodeLanguageMap == null) {
            synchronized (Language.class) {
                if (isoCodeLanguageMap == null) {
                    Map<String, Language> lookupMap = new HashMap<>();
                    for (Language language : Language.values()) {
                        if (lookupMap.containsKey(language.isoCode)) {
                            throw new RuntimeException("duplicate language in the iso code lookup map "+language.isoCode);
                        }

                        lookupMap.put(language.isoCode, language);
                    }
                    isoCodeLanguageMap = Collections.unmodifiableMap(lookupMap);
                }
            }
        }

        return isoCodeLanguageMap.get(isoCode);
    }

    private static Map<Integer, Language> idLanguageMap = null;
    public static Language fromCode(int id) {
        if (idLanguageMap == null) {
            synchronized (Language.class) {
                if (idLanguageMap == null) {
                    Map<Integer, Language> lookupMap = new HashMap<>();
                    for (Language language : Language.values()) {
                        if (lookupMap.containsKey(language.langId)) {
                            throw new RuntimeException("duplicate language id in the lookup map");
                        }

                        lookupMap.put(language.langId, language);
                    }
                    idLanguageMap = Collections.unmodifiableMap(lookupMap);
                }
            }
        }

        return idLanguageMap.get(id);
    }
}
