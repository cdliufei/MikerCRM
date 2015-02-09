package com.mnker.crm.widget.calendar;

import java.util.HashMap;

import android.os.Environment;

public class Constant {

	//public static Header[] header;
	public static String UUID = null;
	
	public static String HOME = "home";
	public static String UPDATE = "update";
	public static String NOTICE = "notice";
	public static String IS_NOTICE = "is_notice";
	
	public static int ERROR_NO = -1;
	
	//public static String HAVE_LOGIN = "haveLogin";

	public static final String LOGIN_SUCCESSED = "login_succeed";
	public static final String JUMP_TO_HOME = "jump_to_home";
	public static final String GET_RECORD = "get_record";
	
//	public static boolean DIALOG_IS_SHOW = false;
//	public static boolean THREAD_NOTICE_ALIVE = false;
	public static boolean THREAD_RECORDLOGIN_ALIVE = false;
	public static String SHARED_NAME = "shared_name";

	public static int ALL_RECORD = 100;
	public static int ALL_EXCEPT_PREVENT_RECORD = 105;
	public static int STAMP_RECORD = 101;
	public static int DAILY_RECORD = 102;
	public static int PREVIOUS_RECORD = 103;
	public static int BOOK_RECORD = 104;
	//public static int BOOK_RECORD = 104; 
	public static int DEFUALT_URL = 3;
	public static int isPhase2 = 1;
	public static int OFFSET = 0;

	public static String PHOTO_PATH = Environment.getExternalStorageDirectory() + "/tamahiyo";
	public static String PHOTO_PATH_HIGH = Environment.getExternalStorageDirectory() + "/tamahiyo/high";
	public static String PHOTO_PATH_MIDDLE = Environment.getExternalStorageDirectory() + "/tamahiyo/middle";
	public static String PHOTO_PATH_LOW = Environment.getExternalStorageDirectory() + "/tamahiyo/low";

	public static String station_filePath = Environment.getExternalStorageDirectory() + "/tempJson.txt";

	public static String KENSHIN_LIST = "kenshin_list";
	public static String BIRTH_LIST = "birth_list";
	public static String BIRTH_CHECK_LIST = "birth_check_list";


	public static String DIALY_LIST = "dialy_list";
	public static String LIFE_LIST = "life_list";
	public static String DISTURBING_LIST = "disturbing_list";
	public static String SCHEDULE_LIST = "schedule_list";
	public static String EVENT_LIST = "event_list";
	public static String MONSHIN_LIST = "monshin_list";
	public static String CARE_FOOD_LIST = "care_food_list";
	public static String Image_LIST = "image_list";


	public static boolean isRecover = false;

	public static String DIARY = "diary";
	public static String BOOK = "book";
	public static String OTHER = "other";


	public static HashMap<String, String> ENGLISH_JAPANESE_MAP = null;
	public static HashMap<String, HashMap<String, String>> ENGLISH_JAPANESE_MAP_ACTION = null;
	public static HashMap<String, String> units = null;
	public static HashMap<String, String[]> items = null;
	public static HashMap<String, String[]> events = null;
	public static HashMap<String, String[]> confirmors = null;
	public static HashMap<String, String[]> defecationForms = null;
	public static HashMap<String, String[]> foods = null;
	public static HashMap<String, String[]> imags = null;
	public static HashMap<String, String[]> illness = null;

	public static class Common {
		public static String DIALY_LIST = "dialy_list";
		public static String STAMP_ID = "stamp_id";
		public static String MEMO = "memo";
		public static String ACTION_DT = "action_dt";
		public static String DISP_DIV = "disp_div";

		public static String APPLI_PK = "appli_pk";
		// public static String SEQ_NO = "seq_no";

		// public static String CHK_KBN = "chk_kbn";
		// public static String CHK_RESULT = "chk_result";
		/*
		 * public static String PREGNANT__WEEK_CNT = "pregnant_week_cnt"; public
		 * static String PREGNANT__WEEK_CNT = "pregnant_week_cnt"; public static
		 * String PREGNANT__WEEK_CNT = "pregnant_week_cnt"; public static String
		 * PREGNANT__WEEK_CNT = "pregnant_week_cnt"; public static String
		 * PREGNANT__WEEK_CNT = "pregnant_week_cnt";
		 */
	}

	public static class DialyListClass {
		public static String SEQ_NO = "seq_no";

	}

	public static class KenshinListClass {
		public static String PREGNANT_WEEK_CNT = "pregnant_week_cnt";
		public static String WEIGHT = "weight";
		public static String FUNDUS_HEIGHT = "fundus_height";
		public static String ABDOMEN = "abdomen";
		public static String BLOOD_PRESSURE1 = "blood_pressure1";
		public static String BLOOD_PRESSURE2 = "blood_pressure2";
		public static String SWELLING = "swelling";
		public static String URINARY_PROTEIN = "urinary_protein";
		public static String URINARY_SUGAR = "urinary_sugar";
		public static String ESTIMATE_WEIGHT = "estimate_weight";

	}

	public static class BirthListClass {
		public static String PREGNANT_DURATION_WEEK = "pregnant_duration_week";
		public static String PREGNANT_DURATION_DAY = "pregnant_duration_day";
		public static String ESTIMATED_TIME_HOUR = "estimated_time_hour";
		public static String ESTIMATED_TIME_MINUTE = "estimated_time_minute";
		public static String PROGRESS = "progress";
		public static String DELIVERY_START = "delivery_start";
		public static String SHOW = "show";
		public static String BLEEDING = "bleeding";
		public static String MULTI_PREGNANCY = "multi_pregnancy";

		public static String BIRTH_CHECK_LIST = "birth_check_list";
		public static String CHK_KBN = "chk_kbn";
		public static String CHK_RESULT = "chk_result";
	}

	/*
	 * public static class BIRTH_CHECK_LIST_CLASS{ public static String CHK_KBN
	 * = "chk_kbn"; public static String CHK_RESULT = "chk_result";
	 *
	 * }
	 */

	/*
	 * public static class DIALY_LIST_CLASS{ public static String SEQ_NO =
	 * "seq_no";
	 *
	 *
	 * }
	 */

	public static class LifeListClass {
		public static String DEFECATION_FORM = "defecation_form";
		public static String FOOD = "food";
		public static String SPORT = "sport";
		public static String HOUSEWORK = "housework";
		public static String WEIGHT = "weight";
		public static String BODY_TEMP = "body_temp";
		public static String SPORT_TIME = "sport_time";

		public static String LIFE_CHECK_LIST = "life_check_list";
		public static String CHK_KBN = "chk_kbn";
		public static String CHK_RESULT = "chk_result";


	}

	public static class DisturbingListClass {
		public static String SEQ_NO = "seq_no";
		public static String BODY_TEMP = "body_temp";
		public static String VOMIT_LEVEL = "vomit_level";
		public static String DEFECATION_FORM = "defecation_form";
		public static String DEFECATION_COLOR = "defecation_color";
		public static String QUANTITY = "quantity";
		public static String URINE_COLOR = "urine_color";
		public static String ILLNESS = "illness";
		public static String DRUG = "drug";
		public static String HOSPITAL = "hospital_pharmacy";
		public static String PHARMACY = "hospital_pharmacy";

		public static String DISTURBING_CHECK_LIST = "disturbing_check_list";
		public static String CHK_KBN = "chk_kbn";
		public static String CHK_RESULT = "chk_result";

	}

	public static class ScheduleListClass {
		public static String SEQ_NO = "seq_no";

	}

	public static class EventListClass {
		public static String SEQ_NO = "seq_no";
		public static String EVENT_ID = "event_id";
		public static String MONTH_AGE_YEAR = "month_age_year";
		public static String MONTH_AGE_MONTH = "month_age_month";

	}

	public static class MonshinListClass {
		public static String SEQ_NO = "seq_no";
		public static String MONTH_AGE = "month_age";
		public static String WEIGHT = "weight";
		public static String HEIGHT = "height";
		public static String HEAD = "head";
		public static String CHEST = "chest";

		public static String MONSHIN_CHECK_LIST = "monshin_check_list";
		public static String MONSHIN_ID = "monshin_id";
		public static String CHK_RESULT = "chk_result";

	}

	public static class CareFoodListClass {
		public static String SEQ_NO = "seq_no";
		public static String SIDE = "side";
		public static String TIME = "time";
		public static String AMOUNT = "amount";
		public static String QUANTITY = "quantity";
		public static String CONFIRMOR = "confirmor";
		public static String DEFECATION_FORM = "defecation_form";
		public static String DEFECATION_COLOR = "defecation_color";
		public static String WEIGHT = "weight";
		public static String FOOD = "food";

	}

	public static class ImageListClass {
		public static String SEQ_NO = "seq_no";
		public static String IMG_PATH = "img_path";
		public static String MONTH = "month";
		public static String IMG_KIND = "img_kind";
		public static String MONTH_AGE_YEAR = "month_age_year";
		public static String MONTH_AGE_MONTH = "month_age_month";

		public static String PREGNANT__WEEK_CNT = "pregnant_week_cnt";
		public static String ONE_TIME_PWD = "one_time_pwd";

	}

	public static class SonRecord {
		public static String PIC_ADDRESS = "img_kind";
		public static String ITEM = "item";
		public static String AGE = "age";
	}

	public static class SonLife {
		public static String WEIGHT = "weight";
		public static String LEFT_RIGHT = "side";
		public static String NEED_TIME = "time";
		public static String MEASURE = "amount";
		public static String CHANGE_PEOPLE = "confirmor";
		public static String URINE_VOLUME = "quantity";
		public static String SHIT_SHAPE = "defecation_form";
		public static String SHIT_COLOR = "defecation_color";
		public static String IN_PEOPLE = "confirmor";
		public static String WHICH_PEOPLE = "which_people";
		public static String MILK_FOOD = "food";
	}

	public static class SonHealth {
		public static String BODY = "disturbing_check_list";
		public static String FEVER = "body_temp";
		public static String DEGREE = "vomit_level";
		public static String SHIT_SHAPE = "defecation_form";
		public static String SHIT_COLOR = "defecation_color";
		public static String URINE_VOLUME = "quantity";
		public static String URINE_COLOR = "urine_color";
		public static String SKIN = "disturbing_check_list";
		public static String EAR_THROAT = "disturbing_check_list";
		public static String DISEASE_TYPES = "illness";
		public static String MEDICAL = "hospital_pharmacy";
		public static String DRUG_NAME = "drug";
		public static String PHARMACY = "hospital_pharmacy";
	}

}
