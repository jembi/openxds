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
/* 
	calendar-cs-win.js
	language: Czech
	encoding: windows-1250
	author: Lubos Jerabek (xnet@seznam.cz)
	        Jan Uhlir (espinosa@centrum.cz)
*/

// ** I18N
Calendar._DN  = new Array('Neděle','Pondělí','Úterý','Středa','Čtvrtek','Pátek','Sobota','Neděle');
Calendar._SDN = new Array('Ne','Po','Út','St','Čt','Pá','So','Ne');
Calendar._MN  = new Array('Leden','Únor','Březen','Duben','Květen','Červen','Červenec','Srpen','Září','Říjen','Listopad','Prosinec');
Calendar._SMN = new Array('Led','Úno','Bře','Dub','Kvě','Črv','Čvc','Srp','Zář','Říj','Lis','Pro');

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "O komponentě kalendář";
Calendar._TT["TOGGLE"] = "Změna prvního dne v týdnu";
Calendar._TT["PREV_YEAR"] = "Předchozí rok (přidrž pro menu)";
Calendar._TT["PREV_MONTH"] = "Předchozí měsíc (přidrž pro menu)";
Calendar._TT["GO_TODAY"] = "Dnešní datum";
Calendar._TT["NEXT_MONTH"] = "Další měsíc (přidrž pro menu)";
Calendar._TT["NEXT_YEAR"] = "Další rok (přidrž pro menu)";
Calendar._TT["SEL_DATE"] = "Vyber datum";
Calendar._TT["DRAG_TO_MOVE"] = "Chyť a táhni, pro přesun";
Calendar._TT["PART_TODAY"] = " (dnes)";
Calendar._TT["MON_FIRST"] = "Ukaž jako první Pondělí";
//Calendar._TT["SUN_FIRST"] = "Ukaž jako první Neděli";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"For latest version visit: http://www.dynarch.com/projects/calendar/\n" +
"Distributed under GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details." +
"\n\n" +
"Výběr datumu:\n" +
"- Use the \xab, \xbb buttons to select year\n" +
"- Použijte tla�?ítka " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " k výběru měsíce\n" +
"- Podržte tla�?ítko myši na jakémkoliv z těch tla�?ítek pro rychlejší výběr.";

Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Výběr �?asu:\n" +
"- Klikněte na jakoukoliv z �?ástí výběru �?asu pro zvýšení.\n" +
"- nebo Shift-click pro snížení\n" +
"- nebo klikněte a táhněte pro rychlejší výběr.";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Zobraz %s první";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Zavřít";
Calendar._TT["TODAY"] = "Dnes";
Calendar._TT["TIME_PART"] = "(Shift-)Klikni nebo táhni pro změnu hodnoty";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "d.m.yy";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %b %e";

Calendar._TT["WK"] = "wk";
Calendar._TT["TIME"] = "Čas:";
