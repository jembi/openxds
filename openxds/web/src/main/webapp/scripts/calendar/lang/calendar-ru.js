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

// Calendar RU language
// Translation: Sly Golovanov, http://golovanov.net, <sly@golovanov.net>
// Encoding: any
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("во�?кре�?енье",
 "понедельник",
 "вторник",
 "�?реда",
 "четверг",
 "п�?тница",
 "�?уббота",
 "во�?кре�?енье");

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
("в�?к",
 "пон",
 "втр",
 "�?рд",
 "чет",
 "п�?т",
 "�?уб",
 "в�?к");

// full month names
Calendar._MN = new Array
("�?нварь",
 "февраль",
 "март",
 "апрель",
 "май",
 "июнь",
 "июль",
 "авгу�?т",
 "�?ент�?брь",
 "окт�?брь",
 "но�?брь",
 "декабрь");

// short month names
Calendar._SMN = new Array
("�?нв",
 "фев",
 "мар",
 "апр",
 "май",
 "июн",
 "июл",
 "авг",
 "�?ен",
 "окт",
 "но�?",
 "дек");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "О календаре...";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"For latest version visit: http://www.dynarch.com/projects/calendar/\n" +
"Distributed under GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details." +
"\n\n" +
"Как выбрать дату:\n" +
"- При помощи кнопок \xab, \xbb можно выбрать год\n" +
"- При помощи кнопок " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " можно выбрать ме�?�?ц\n" +
"- Подержите �?ти кнопки нажатыми, чтобы по�?вило�?ь меню бы�?трого выбора.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Как выбрать врем�?:\n" +
"- При клике на ча�?ах или минутах они увеличивают�?�?\n" +
"- при клике �? нажатой клавишей Shift они уменьшают�?�?\n" +
"- е�?ли нажать и двигать мышкой влево/вправо, они будут мен�?ть�?�? бы�?трее.";

Calendar._TT["PREV_YEAR"] = "�?а год назад (удерживать дл�? меню)";
Calendar._TT["PREV_MONTH"] = "�?а ме�?�?ц назад (удерживать дл�? меню)";
Calendar._TT["GO_TODAY"] = "Сегодн�?";
Calendar._TT["NEXT_MONTH"] = "�?а ме�?�?ц вперед (удерживать дл�? меню)";
Calendar._TT["NEXT_YEAR"] = "�?а год вперед (удерживать дл�? меню)";
Calendar._TT["SEL_DATE"] = "Выберите дату";
Calendar._TT["DRAG_TO_MOVE"] = "Перета�?кивайте мышкой";
Calendar._TT["PART_TODAY"] = " (�?егодн�?)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Первый день недели будет %s";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Закрыть";
Calendar._TT["TODAY"] = "Сегодн�?";
Calendar._TT["TIME_PART"] = "(Shift-)клик или нажать и двигать";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%e %b, %a";

Calendar._TT["WK"] = "нед";
Calendar._TT["TIME"] = "Врем�?:";
