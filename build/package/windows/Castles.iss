;This file will be executed next to the application bundle image
;I.e. current directory will contain folder Castles with application files
[Setup]
AppId={{fxApplication}}
AppName=Castles
AppVersion=1.0
AppVerName=Castles 1.0
AppPublisher=LarryPassantino
AppComments=Castles
AppCopyright=Copyright (C) 2016
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
UsePreviousAppDir=No
DefaultDirName={localappdata}\Castles
DisableStartupPrompt=Yes
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=LarryPassantino
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=Castles
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=Castles\Castles.ico
UninstallDisplayIcon={app}\Castles.ico
UninstallDisplayName=Castles
WizardImageStretch=No
WizardSmallImageFile=Castles-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "Castles\Castles.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "Castles\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Castles"; Filename: "{app}\Castles.exe"; IconFilename: "{app}\Castles.ico"; Check: returnTrue()
Name: "{commondesktop}\Castles"; Filename: "{app}\Castles.exe";  IconFilename: "{app}\Castles.ico"; Check: returnFalse()


[Run]
Filename: "{app}\Castles.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\Castles.exe"; Description: "{cm:LaunchProgram,Castles}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\Castles.exe"; Parameters: "-install -svcName ""Castles"" -svcDesc ""Castles"" -mainExe ""Castles.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\Castles.exe "; Parameters: "-uninstall -svcName Castles -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
