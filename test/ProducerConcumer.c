#include "syscall.h"

int buffer[100];

int p,q;

int mutex,fillCount,emptyCount;


void produce(int a){
	while(true){
		SemaphoreP(emptyCount);
		SemaphoreP(mutex);
		buffer[p] = a;
		if(p == 99){
			p = 0;
		}
		else p++;
		SemaphoreV(mutex);
		SemaphoreV(fillCount);
	}
}

void consume(){
	int val;
	while(true){
		SemaphoreP(fillCount);
		SemaphoreP(mutex);
		val = buffer[q];
		q++;
		if(q == 99){
			q=0;
		}
		else q++;
		SemaphoreV(mutex);
		SemaphoreV(emptyCount);
	}
}

int main(){
	mutex = SemaphoreGet(1);
	fillCount = SemaphoreGet(0);
	emptyCount = SemaphoreGet(100);
	return 0;
}