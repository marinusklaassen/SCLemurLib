/*
* FILENAME: LemurEditorDemo
*
* DESCRIPTION:
*         A bit of automatic UI build procedures to demo the LemurEditor class.
*
* AUTHOR: Marinus Klaassen (2021Q2)
*
*/

/*

buildBind { | bindGui, pageName, id = 1|
		var type,snippets,idNames,typeArray,parNames;
		typeArray = bindGui.typeArray; parNames = bindGui.nameArray;

		if ( typeArray.size == parNames.size, {
			snippets = snippets ++ this.addPageCode(pageName);

			parNames do: { |name, i|
				var type = typeArray[i];

				case
				{ type == \Fader }
				{       idNames = idNames ++ ["p" ++ id ++ "Fader" ++ i];
					snippets = snippets ++ this.faderCode(pageName,idNames.last,i * 100 + 6,15,100, 678);
				}
				{ type == \Range }
				{       idNames = idNames ++ ["p" ++ id ++ "Range" ++ i];
					snippets = snippets ++ this.rangeCode(pageName,idNames.last,i * 100 + 6,15,100, 678);
				};

				snippets = snippets ++ this.textCode(pageName,"p" ++ id ++ "Text" ++ i,name,i * 100 + 6,129,100,48);
			};
			buildInfo[id, \pageName] = pageName;
			buildInfo[id, \idNames] = idNames;

			this.sendBuildRequest("<JZML>" ++ snippets ++ "<JZML>");

			r {
				0.1.wait;
				lemurConnection.oscNetAddr.sendMsg("/interface", pageName); // TODO add connection check
				bindGui.randomize;

			}.play;
		}, { "input typeArray and oscTagArray don't have the same sizes".postln; });
	}

	buildDemo { | typeArray, parNames, pageName = "default page", id = 1|
		var type,snippets,idNames;

		if ( typeArray.size == parNames.size, {
			snippets = snippets ++ this.addPageCode(pageName);

			parNames do: { |name, i|
				var type = typeArray[i];

				case
				{ type == \Fader }
				{       idNames = idNames ++ ["p" ++ id ++ "Fader" ++ i];
					snippets = snippets ++ this.faderCode(pageName,idNames.last,i * 100 + 6,15,100, 678);
				}
				{ type == \Range }
				{       idNames = idNames ++ ["p" ++ id ++ "Range" ++ i];
					snippets = snippets ++ this.rangeCode(pageName,idNames.last,i * 100 + 6,15,100, 678);
				};

				snippets = snippets ++ this.textCode(pageName,"p" ++ id ++ "Text" ++ i,name,i * 100 + 6,129,100,48);
			};
			buildInfo[id, \pageName] = pageName;
			buildInfo[id, \idNames] = idNames;

			this.sendBuildRequest("<JZML>" ++ snippets ++ "<JZML>");

			r {
				0.1.wait;
				lemurConnection.oscNetAddr.sendMsg("/interface", pageName);// add conection check
				nil;
			}.play;
		}, { "input typeArray and oscTagArray don't have the same sizes".postln; });
	}

*/