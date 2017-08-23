package utils;

import java.util.Map;
import java.util.regex.Pattern;

import model.Holiday;
import model.Worktime;

public final class ValidateUtils {

	private ValidateUtils() {
	}

	/**
	 * <p>
	 * 檢查字串是否為空格、空白字元或 null
	 * </p>
	 *
	 * <pre>
	 * ValidateUtils.isBlank(null)      = true
	 * ValidateUtils.isBlank("")        = true
	 * ValidateUtils.isBlank(" ")       = true
	 * ValidateUtils.isBlank("bob")     = false
	 * ValidateUtils.isBlank("  bob  ") = false
	 * </pre>
	 */
	public static boolean isBlank(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static boolean isLegalPassWord(String value) {
		return value.matches("\\w{6,16}");
	}

	/**
	 * <p>
	 * 檢查字串是否為自然數(正整數)
	 * </p>
	 */
	public static boolean isNaturalNumbers(String number) {
		return number.matches("\\d*") && Integer.parseInt(number) > 0;
	}

	/**
	 * <p>
	 * 檢查字串是否為正浮點數
	 * </p>
	 */
	public static boolean isPositiveDouble(String number) {
		return number.matches("\\d*|\\d*\\.\\d*") && Double.parseDouble(number) > 0;
	}

	/******************************* 以下吳軒穎 **************************************/
	public static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+\\.*\\w+@(\\w+\\.){1,5}[a-zA-Z]{2,3}$");

	/**
	 * Email 格式檢查程式
	 * 
	 * @since 2006/07/19
	 **/
	public static boolean isValidEmail(String email) {
		boolean result = false;
		if (EMAIL_PATTERN.matcher(email).matches()) {
			result = true;
		}
		return result;
	}

	/**
	 * <p>
	 * 檢查身分證是否合法
	 * </p>
	 */
	public static boolean isValidPID(String id) {
		int[] num = new int[10];
		int[] rdd = { 10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19, 20, 21, 22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30,
				31, 33 };
		if(id.length()!=10){
			return false;
		}
		id = id.toUpperCase();
		if (id.charAt(0) < 'A' || id.charAt(0) > 'Z') {
			System.out.println("第一個字錯誤!!");
			return false;
		}
		if (id.charAt(1) != '1' && id.charAt(1) != '2') {
			System.out.println("第二個字錯誤!!");
			return false;
		}
		for (int i = 1; i < 10; i++) {
			if (id.charAt(i) < '0' || id.charAt(i) > '9') {
				System.out.println("輸入錯誤!!");
				return false;
			}
		}
		for (int i = 1; i < 10; i++) {
			num[i] = (id.charAt(i) - '0');
		}
		num[0] = rdd[id.charAt(0) - 'A'];
		int sum = ((int) num[0] / 10 + (num[0] % 10) * 9);
		for (int i = 0; i < 8; i++) {
			sum += num[i + 1] * (8 - i);
		}
		if (10 - sum % 10 == num[9]) {
			return true;
		} else {
			return false;
		}
	}

	/******************************* 以上吳軒穎 **************************************/

	/******************************* 以下陳民錞 **************************************/
	public static boolean isLegalWorktime(Worktime worktime, Map<String, Holiday> holidays) {
		int SunNormalTime=worktime.getSunNormal(),
				SunOverTime=worktime.getSunOvertime();
		int MonNormalTime=worktime.getMonNormal(),
				MonOverTime=worktime.getMonOvertime();
		int TueNormalTime=worktime.getTueNormal(),
				TueOverTime=worktime.getTueOvertime();
		int WedNormalTime=worktime.getWedNormal(),
				WedOverTime=worktime.getWedOvertime();
		int ThuNormalTime=worktime.getThuNormal(),
				ThuOverTime=worktime.getThuOvertime();
		int FriNormalTime=worktime.getFriNormal(),
				FriOverTime=worktime.getFriOvertime();
		int SatNormalTime=worktime.getSatNormal(),
				SatOverTime=worktime.getSatOvertime();
		
		// 星期日
		if (holidays.get("sun") == null) {
			//沒假日
			if(SunNormalTime>8||SunNormalTime<0)
				return false;
			if(SunOverTime>4||SunOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("sun").getHours();
			if(SunNormalTime>(8-holidayTime)||SunNormalTime<0)
				return false;
			if(SunOverTime>4||SunOverTime<0)
				return false;
		}
		// 星期一
		if (holidays.get("mon") == null) {
			//沒假日
			if(MonNormalTime>8||MonNormalTime<0)
				return false;
			if(MonOverTime>4||MonOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("mon").getHours();
			if(MonNormalTime>(8-holidayTime)||MonNormalTime<0)
				return false;
			if(MonOverTime>4||MonOverTime<0)
				return false;
		}
		// 星期二
		if (holidays.get("tue") == null) {
			//沒假日
			if(TueNormalTime>8||TueNormalTime<0)
				return false;
			if(TueOverTime>4||TueOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("tue").getHours();
			if(TueNormalTime>(8-holidayTime)||TueNormalTime<0)
				return false;
			if(TueOverTime>4||TueOverTime<0)
				return false;
		}
		// 星期三
		if (holidays.get("wed") == null) {
			//沒假日
			if(WedNormalTime>8||WedNormalTime<0)
				return false;
			if(WedOverTime>4||WedOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("wed").getHours();
			if(WedNormalTime>(8-holidayTime)||WedNormalTime<0)
				return false;
			if(WedOverTime>4||WedOverTime<0)
				return false;
		}
		// 星期四
		if (holidays.get("thu") == null) {
			//沒假日
			if(ThuNormalTime>8||ThuNormalTime<0)
				return false;
			if(ThuOverTime>4||ThuOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("thu").getHours();
			if(ThuNormalTime>(8-holidayTime)||ThuNormalTime<0)
				return false;
			if(ThuOverTime>4||ThuOverTime<0)
				return false;
		}
		// 星期五
		if (holidays.get("fri") == null) {
			//沒假日
			if(FriNormalTime>8||FriNormalTime<0)
				return false;
			if(FriOverTime>4||FriOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("fri").getHours();
			if(FriNormalTime>(8-holidayTime)||FriNormalTime<0)
				return false;
			if(FriOverTime>4||FriOverTime<0)
				return false;
		}
		// 星期六
		if (holidays.get("sat") == null) {
			//沒假日
			if(SatNormalTime>8||SatNormalTime<0)
				return false;
			if(SatOverTime>4||SatOverTime<0)
				return false;
		}else{
			int holidayTime=holidays.get("sat").getHours();
			if(SatNormalTime>(8-holidayTime)||SatNormalTime<0)
				return false;
			if(SatOverTime>4||SatOverTime<0)
				return false;
		}
		return true;

	}

	/******************************* 以上陳民錞 **************************************/

}








