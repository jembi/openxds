/*
 *
 *  Copyright (C) 2009 SYSNET International <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
// ** I18N

// Calendar EN language
// Author: Mihai Bazon, <mihai_bazon@yahoo.com>
// Translation: Yourim Yi <yyi@yourim.net>
// Encoding: EUC-KR
// lang : ko
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names

Calendar._DN = new Array
("�?�요�?�",
 "월요�?�",
 "화요�?�",
 "수요�?�",
 "목요�?�",
 "금요�?�",
 "토요�?�",
 "�?�요�?�");

// Please note that the following array of short day names (and the same goes
// for short month names, _SMN) isn't absolutely necessary.  We give it here
// for exemplification on how one can customize the short day names, but if
// they are simply the first N letters of the full name you can simply say:
//
//   Calendar._SDN_len = N; // short day name length
//   Calendar._SMN_len = N; // short month name length
//
// If N = 3 then this is not needed either since we assume a value of 3 if not
// present, to be compatible with translation files that were written before
// this feature.

// short day names
Calendar._SDN = new Array
("�?�",
 "월",
 "화",
 "수",
 "목",
 "금",
 "토",
 "�?�");

// full month names
Calendar._MN = new Array
("1월",
 "2월",
 "3월",
 "4월",
 "5월",
 "6월",
 "7월",
 "8월",
 "9월",
 "10월",
 "11월",
 "12월");

// short month names
Calendar._SMN = new Array
("1",
 "2",
 "3",
 "4",
 "5",
 "6",
 "7",
 "8",
 "9",
 "10",
 "11",
 "12");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "calendar �? 대해서";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"\n"+
"최신 버전�?� 받으시려면 http://www.dynarch.com/projects/calendar/ �? 방문하세요\n" +
"\n"+
"GNU LGPL �?��?�센스로 배�?��?�니다. \n"+
"�?��?�센스�? 대한 �?세한 내용�?� http://gnu.org/licenses/lgpl.html �?� �?�으세요." +
"\n\n" +
"날짜 선�?:\n" +
"- 연�?�를 선�?하려면 \xab, \xbb 버튼�?� 사용합니다\n" +
"- 달�?� 선�?하려면 " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " 버튼�?� 누르세요\n" +
"- 계�? 누르고 있으면 위 값들�?� 빠르게 선�?하실 수 있습니다.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"시간 선�?:\n" +
"- 마우스로 누르면 시간�?� �?가합니다\n" +
"- Shift 키와 함께 누르면 �?소합니다\n" +
"- 누른 �?태�?서 마우스를 움�?�?�면 좀 �?� 빠르게 값�?� 변합니다.\n";

Calendar._TT["PREV_YEAR"] = "지난 해 (길게 누르면 목�?)";
Calendar._TT["PREV_MONTH"] = "지난 달 (길게 누르면 목�?)";
Calendar._TT["GO_TODAY"] = "오늘 날짜로";
Calendar._TT["NEXT_MONTH"] = "다�?� 달 (길게 누르면 목�?)";
Calendar._TT["NEXT_YEAR"] = "다�?� 해 (길게 누르면 목�?)";
Calendar._TT["SEL_DATE"] = "날짜를 선�?하세요";
Calendar._TT["DRAG_TO_MOVE"] = "마우스 드래그로 �?��?� 하세요";
Calendar._TT["PART_TODAY"] = " (오늘)";
Calendar._TT["MON_FIRST"] = "월요�?��?� 한 주�?� 시작 요�?�로";
Calendar._TT["SUN_FIRST"] = "�?�요�?��?� 한 주�?� 시작 요�?�로";
Calendar._TT["CLOSE"] = "닫기";
Calendar._TT["TODAY"] = "오늘";
Calendar._TT["TIME_PART"] = "(Shift-)�?�릭 �?는 드래그 하세요";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%b/%e [%a]";

Calendar._TT["WK"] = "주";
