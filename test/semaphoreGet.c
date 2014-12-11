#include "syscall.h"

int main(){
	int s = semaphoreGet(0);
	semaphoreP(s);
	return 0;
}