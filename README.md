# StudyScheduler
The Study-Scheduler takes a list of books and chapters, then schedules a desired number of chapters in a quadratic interval. In other words, if a new chapter is selected to study, this chapter will reoccur less and less frequently as the weeks go on. This follows the methodology that the more times a subject's content is studied, the less frequent the intervals that it needs re-review.

# Pre-requisites to run:
(1) The data/ folder needs to exist and contain the ResourceData with the study material you want to use.
(2) The output/ folder needs to exist, but it doesn't need to contain anything.

# Commands: 
**To Run:** java generate run
**To Test:** java generate test
  
# Resource Data Syntax:
BookName;Author;NumberOfChapters;Ch1Problems,Ch2Problems,...,LastChProblems;IsEnabledFlag;SubjectType;

**Example**: Calculus;Spivak;30;25,28,28,23,42,17,21,20,30,35,71,27,40,30,33,2,0,49,49,28,8,33,30,31,12,10,19,11,2,5;T;MATH;
