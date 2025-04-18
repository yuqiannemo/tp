---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# RecruitTrack Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the [AddressBook-Level3](https://se-education.org/addressbook-level3/) project created by the SE-EDU initiative

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete id/1 --force`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is defined in [`Ui.java`](https://github.com/AY2425S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java), and implemented by [`UiManager`](https://github.com/AY2425S2-CS2103T-W09-1/tp/blob/master/src/main/java/seedu/address/ui/UiManager.java).

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>


**Structure**:
The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

- `CommandBox`: Accepts user input commands.
- `ResultDisplay`: Shows feedback for executed commands.
- `PersonListPanel`: Displays a list of person objects (each representing a candidate).
- `StatusBarFooter`: Shows save location information.
- `HelpWindow`: A separate pop-up window with a user guide link.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The main controller [`UiManager`](https://github.com/se-edu/addressbook-level3/blob/master/src/main/java/seedu/address/ui/UiManager.java) initializes these UI parts through `MainWindow`, binds them to model data from `Logic`, and handles fatal errors.

**Interactions**:
The `UI` component,
* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("summary j/SWE")` API call as an example.

<puml src="diagrams/SummarySequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `SummaryCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `SummaryCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `SummaryCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to summarize applicants).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="750" />


The `Model` component,

* stores the address book data i.e., all `Applicant` objects (which are contained in a `UniqueApplicantList` object).
* stores the currently 'selected' `Applicant` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Applicant>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-W09-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* HR teams handling candidate pipelines
* Manages multiple candidate pipelines
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* are data-driven decision makers
* works in a face-paced environment

**Value proposition**: Simplifies candidate tracking by organizing applicant information,
communication history, and hiring stages for quick access in a fast, no-frills CLI environment


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                            | I want to …​                                                                                 | So that I can…​                                       |
|----------|------------------------------------|----------------------------------------------------------------------------------------------|-------------------------------------------------------|
| `* * *`	 | user	                              | specify the stage of the application process each applicant is in	                           | keep track of each applicant’s progress               |
| `* * *`	 | user	                              | filter by an applicant’s application process	                                                | keep track of which applicants are left in each stage |
| `* * *`	 | user	                              | search for a candidate by name or job position	                                              | quickly find relevant information                     |
| `* * *`	 | user	                              | view a summary of all candidates in a specific hiring stage	                                 | keep track of the statistics easily                   |
| `* * *`	 | user	                              | view statistics (e.g., number of hired candidates, time needed in each stage)	               | evaluate my recruitment process                       |
| `* * *`	 | user	                              | sort candidates by the date they applied	                                                    | review the most recent applications first             |
| `* * *`	 | user	                              | add candidates’ details	                                                                     | track them easily                                     |
| `* * *`	 | user	                              | view a candidate’s status	                                                                   | know their hiring progress                            |
| `* * *`	 | user	                              | move candidates to different stages	                                                         | manage the hiring process                             |
| `* * `   | user                               | group applicants based on what jobs they are applying for                                    | manage applicants from different jobs more easily     |
| `* *`    | 	power user	                       | delete multiple candidates with a single command                                             | 	keep my database clean                               |
| `* *`    | 	power user	                       | tag candidates	                                                                              | easily classify and retrieve important applicants     |
| `* *`    | user who is not familiar with CLIs | have easy access to a list of supported commands, especially the commonly used ones          | get up to speed with using the CLI                    |
| `* *`	   | user	                              | attach a photo to each applicant	                                                            | identify the applicants easily                        |
| `* *`	   | user	                              | bulk update candidate statuses	                                                              | save time                                             |
| `* *`	   | user	                              | export candidate data as a CSV file	                                                         | analyze and share information easily with my team     |
| `* *`	   | user	                              | add a simple rating (e.g., 1-5 stars) to candidates	                                         | quickly assess their potential                        |
| `*`	     | user	                              | search for particular keywords in past communication history	                                | revisit specific conversations I’ve had               |
| `*`	     | long-time user	                    | delete communication history for specific contacts	                                          | free up storage space when needed                     |
| `*`	     | long-time user	                    | have the option to assign new shortcuts or keywords to my frequently used commands	          | navigate the application more efficiently             |
| `*`	     | user	                              | receive notifications when a candidate has been in a stage for too long	                     | take action to move them forward                      |
| `*`	     | user	                              | restore mistakenly deleted candidates within a certain time frame	                           | recover important information if needed               |
| `*`	     | user	                              | integrate RecruitTrack with my email client	                                                 | track all correspondence with candidates in one place |
| `*`	     | user	                              | generate customized reports based on hiring trends and candidate performance	                | make data-driven recruitment decisions                |
| `*`	     | user	                              | send an email template from the CLI	                                                         | quickly communicate with candidates                   |
| `*`	     | user	                              | view a candidate’s resume and notes before an interview	                                     | prepare relevant questions                            |


### Use cases

(For all use cases below, the **System** is the `RecruitTrack` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add an applicant**

**MSS:**

1.  User requests to add an applicant by providing the details.
2.  RecruitTrack adds the applicant with the details into the list.

    Use case ends.

**Extensions:**

* 1a. The details don't follow the correct format.

    * 1a1. RecruitTrack shows an error message.

* 1b. Lacks mandatory details.

    * 1b1. RecruitTrack shows an error message.

<br>

**Use case: UC02 - Delete an applicant**

**MSS:**

1.  User requests to list applicants.
2.  RecruitTrack shows a list of applicants.
3.  User requests to delete a specific applicant.
4.  RecruitTrack deletes the applicant.

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 3a. There is no matching applicant given the user's request.

    * 3a1. RecruitTrack shows an error message.

      Use case resumes from step 2.

<br>

**Use case: UC03 - Edit an applicant**

**MSS:**

1.  User requests to list applicants.
2.  RecruitTrack shows a list of applicants.
3.  User requests to edit a specific applicant in the list and provides the new details for that applicant.
4.  RecruitTrack edits the applicant's details.

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 3a. There is no matching applicant given the user's request.

    * 3a1. RecruitTrack shows an error message.

      Use case resumes from step 2.

* 3b. The new details don't follow the correct format.

    * 3b1. RecruitTrack shows an error message.

      Use case resumes from step 2.

<br>

**Use case: UC04 - Update an applicant**

* Similar to UC03 except the user can only update status of the applicant.

<br>

**Use case: UC05 - Search an applicant**

**MSS:**

1.  User requests to search applicant(s) by providing their details.
2.  RecruitTrack finds applicant(s) with the given details.
3.  RecruitTrack returns a list of applicants that match the details.

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 2b. No applicant with the given details is found.

    * 2b1. RecruitTrack indicates that no applicant is found.

    Use case ends.

<br>

**Use case: UC06 - Rate an applicant**

**MSS:**

1.  User requests to list applicants.
2.  RecruitTrack shows a list of applicants.
3.  User requests to rate an applicant by providing the applicant's details and rating number.
4.  RecruitTrack rates applicants with the given details.

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 3b. There is no matching applicant given the user's request.

    * 3b1. RecruitTrack shows an error message.

      Use case resumes from step 2.

<br>

**Use case: UC07 - Summarize the applicant records without filter**

**MSS:**

1.  User requests to summarize the applicant records without specifying any filter.
2.  RecruitTrack output the summary of all the applicants' details.

<br>

**Use case: UC08 - Summarize the applicant records with filter**

**MSS:**

1.  User requests to summarize the applicant records and specify the filters.
2.  RecruitTrack output the summary of all applicants that meet the specified filters.

<br>

**Use case: UC09 - Export the applicants data**

**MSS:**

1.  User requests to export the applicants data.
2.  RecruitTrack requests the user to specify the location to save the exported data.
3.  RecruitTrack save the exported data in the specify location.

**Extensions:**

* 2a. User didn't specify the save location.

    * 2a1. Export is cancelled.

    Use case ends.

<br>

**Use case: UC10 - Add applicant's profile picture**

**MSS:**

1.  User requests to change an applicant's profile picture.
2.  RecruitTrack requests the user to select an image.
3.  RecruitTrack update the applicant's profile picture with the selected image.

    Use case ends.

**Extensions:**

* 2a. User didn't select an image.

    * 2a1. No change is made to the applicant's profile picture.

    Use case ends.


### Non-Functional Requirements
1. Should run on any mainstream OS (Windows, macOS, Linux) with Java 17 or above installed.
2. Should handle up to 1000 candidates without noticeable lag in typical usage.
3. Common commands (e.g., adding candidates, updating statuses, searching) should execute within 200ms under normal load.
4. A user with above-average typing speed should be able to perform most tasks faster using commands than using a mouse.
5. Should provide clear error messages and usage hints for invalid commands.
6. Candidate data should be stored securely, ensuring unauthorized users cannot access sensitive information.
7. Data should be persistently stored and not lost between sessions.

### Glossary

- **Mainstream OS** - Windows, Linux, Unix, MacOS
- **Private contact detail** - A contact detail that is not meant to be shared with others
- **Applicant** – An applicant who has applied for a job and is stored as a contact in the system.
- **Application Metadata** – Information related to an applicant’s job application, such as job position, status, and contact details.
- **Contact** – A stored applicant or candidate with essential details such as name, email, phone, job position, and application status.
- **Custom Status** – A user-defined application status when the `--custom` flag is used.
- **Error Message** – A notification displayed when an invalid input or operation occurs (e.g., invalid email format, missing required fields).
- **Email Validation** – A process ensuring the provided email follows a valid format (e.g., `user@domain.com`).
- **`--force` Flag** – A command option that allows updating an existing contact if a duplicate email or phone number is found.
- **Identifier Type** – A flag (`n/`, `e/`, `p/`, `id/`) used to specify whether a contact is searched by name, email, phone number, or ID in the last shown list.
- **Job Position** – The role or position that an applicant is applying for, stored in the system.
- **Parameter** – A required or optional value provided in a command (e.g., `NAME`, `EMAIL`, `STATUS`).
- **Status Update** – A change in an applicant’s application stage using the `update` command.
- **System ID** – A unique numerical identifier assigned to each contact in the system.
- **Unique Identifier** – A distinct value (email, phone number, or system ID) used to identify contacts.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.


### Deleting an Applicant
1. Force Deleting an Applicant Using Different Identifiers (ID, Email, Phone, etc.)

   1. Test case: delete id/1 --force<br>
       Expected: The first applicant is deleted from the list. Details of the deleted applicant are shown in the message.
   2. Test case: delete n/John Doe e/johndoe@example.com --force<br>
 Expected: Applicant named "John Doe" with email "johndoe@example.com" is deleted from the list. Details of the deleted applicant are shown in the message.
   3. Test case: delete p/98765432 --force<br>
Expected: Applicant with phone number "98765432" is deleted from the list. Details of the deleted applicant are shown in the message.
   4. Test case: delete bfr/2024-01-01 aft/2024-06-01 --force<br>
Expected: All applicants added after June 1, 2024 and before Jan 1, 2024, are deleted. Details of the deleted applicants are shown in the message.
   5. Test case: delete j/Software Engineer s/Rejected --force<br>
Expected: All applicants with the job position "Software Engineer" and the status "Rejected" are deleted. Details of the deleted applicants are shown in the message.

2. Deleting an Applicant without the --force Flag
   1. Test case: delete id/3<br>
Expected: Confirmation message will be displayed (assuming there are at least 3 applicants in the applicant records)

3. Deleting an Applicant That Does Not Exist
   1. Prerequisites: Ensure no applicants match the given identifiers.
   2. Test cases:
      1. Test case: delete id/0 --force<br>
      Expected: No applicant is deleted. Error message is shown.
      2. Test case: delete n/Nonexistent Applicant --force<br>
Expected: No applicant is deleted. Error message is shown.

4. _{ more test cases …​ }_

### Exporting Applicant Data

1. Exporting list of all applicants

   1. Test case: `export applicants.csv`
      Expected: A CSV file named `applicants.csv` is created in the home directory. It contains details of all currently displayed applicants.
   2. Test case: `export export_folder/candidates.csv`
      Expected: The file `candidates.csv` is created inside the `export_folder` directory (must exist beforehand).

2. Exporting an Empty List

   1. Prerequisites: Ensure the displayed list is empty (e.g., after searching for a non-existent applicant).
   2. Test case: `export empty.csv`
      Expected: A CSV file is created, but it only contains the header row. A message indicating zero applicants exported is shown.

3. Invalid Export File Names

   1. Test case: `export`
      Expected: Error message is shown indicating that the file name is missing.
   2. Test case: `export invalid/file\name.csv`
      Expected: Error message is shown due to invalid file path (OS-dependent).
   3. Test case: `export /root/protected.csv` (Linux/macOS) or `export C:\Windows\System32\protected.csv` (Windows)
      Expected: Error message is shown due to insufficient permissions.

4. Overwriting an Existing File

   1. Prerequisite: Make sure a file with the same name already exists.
   2. Test case: `export applicants.csv`
      Expected: The existing file is overwritten with new content. Message confirms successful export.

### Searching Applicants

1. Searching by Name, Email, Phone, Job Position, or Status

   1. Test case: `search n/Alice`
      Expected: All applicants with names containing "Alice" (case-insensitive, partial match supported) are listed.
   2. Test case: `search e/example.com`
      Expected: All applicants with emails that contain "example.com" are shown.
   3. Test case: `search p/9123`
      Expected: All applicants with phone numbers containing "9123" are displayed.
   4. Test case: `search j/SWE`
      Expected: All applicants applying for positions with "SWE" in the title are shown.
   5. Test case: `search s/Interviewing`
      Expected: All applicants currently in the "Interviewing" status are shown.

2. Searching by Multiple Fields

   1. Test case: `search n/John j/Software`
      Expected: Applicants whose name contains "John" **or** job position contains "Software" are shown.
   2. Test case: `search s/Rejected p/123`
      Expected: Applicants whose status is "Rejected" **or** phone number contains "123" are shown.

3. Search with No Results

   1. Test case: `search n/NotExist`
      Expected: "0 applicants listed!" message shown. Displayed list is empty.

4. Invalid Search Commands

   1. Test case: `search`
      Expected: Error message shown indicating that at least one field must be specified.
   2. Test case: `search x/unknown`
      Expected: Error message shown due to unrecognized prefix `x/`.

### Sorting Applicants

1. Sort by Name, Email, Added Time, Job Position, or Status

    1. Test case: `sort n/`
       Expected: Applicant list has been sorted successfully based on Name.
    2. Test case: `sort e/`
       Expected: Applicant list has been sorted successfully based on Email Address.
    3. Test case: `sort time/`
       Expected: Applicant list has been sorted successfully based on Added Time.
    4. Test case: `sort n/`
       Expected: Applicant list has been sorted successfully based on Job Position.
    5. Test case: `sort s/`
       Expected: Applicant list has been sorted successfully based on Application Status.

2. Invalid sorting criteria

    1. Test case: `sort`
       Expected: Error message shown indicating that sorting criteria field must be specified
    2. Test case: `sort zzz/`
       Expected: Error message shown due to unrecognized criteria `zzz/`.
    3. Test case: `sort n/ e/`
       Expected: Error message shown indicating that only one criterion can be specified.

### Saving and Editing data

1. Applicant details are stored in the `data` folder, in a file named `addressbook.json`.
2. To manually edit the details, you can edit the `addressbook.json` file, but details must strictly follow the original format.
3. If the applicant details in the `addressbook.json` file are corrupted or missing any fields, the application will wipe the stored data on launch, resulting in an empty applicant list.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

Team Size: 5

1. **Preset Application Stages** </br>
    Define a list of preset application stage statuses for users to select from when using `update`. This will also provide support for sorting and filtering of applicants by application stage.
2. **Extend sorting and searching capabilities** </br>
   Allow for more ways to search and sort applicants, such as by their `Tags`.
3. **Improve clarity of error messages** </br>
   Currently, only general error messages are displayed upon inputting invalid command formats. By specifying exactly what parameters are missing or invalid, users will have an easier time identifying what's wrong with their command input.
4. **Built-in Command Summary** </br>
   Instead of only providing a link to the User Guide as a response to the `help` command, we can directly display the command summary as provided in the User Guide, so that users will not have to navigate through the entire User Guide.
