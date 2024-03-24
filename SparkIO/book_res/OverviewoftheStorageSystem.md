Chapter 4 of "深入理解spark：核心思想与源码分析" is dedicated to Spark's storage system. Here is a summary of each section:

### 4.1 Overview of the Storage System

- Introduces Spark's strategy to overcome the bottleneck of disk I/O in Hadoop by prioritizing in-memory storage of data, significantly improving execution efficiency.
- Discusses the implementation of the BlockManager, which is central to Spark's storage system, showing its critical role in both the driver application and executors.

### 4.1.1 Implementation of BlockManager

- BlockManager is described as the core component in Spark's storage system. Its initialization process is detailed, highlighting the creation of a BlockManager instance on both the driver and executors.
- The architecture and components of BlockManager, including shuffle client, memory store, disk store, and others, are discussed.

### 4.1.2 Architecture of Spark's Storage System

- Presents an architectural overview of Spark's storage system, explaining interactions among executor's BlockManager, driver's BlockManager, and various storage layers like MemoryStore, DiskStore, and TachyonStore.
- Explains the role of BlockManager in reading and writing operations, and how it handles storage in different scenarios, including memory insufficiency and accessing remote blocks.

### 4.2 Shuffle Service and Client

- Discusses the necessity of incorporating network service components into the storage system due to Spark's distributed nature, where map task outputs need to be fetched by reduce tasks possibly running on different machines.
- Describes the initialization and functioning of the NettyBlockTransferService and its role in facilitating shuffle operations by managing file transfers across executors.

### 4.3 Management of BlockManager by BlockManagerMaster

- Explores how the BlockManagerMaster on the driver manages BlockManagers on executors, handling registration, updates, and queries about block locations.
- Details on message interactions between executors and the driver through BlockManagerMasterActor for block management tasks.

### 4.4 Disk Block Manager

- Describes DiskBlockManager's role in managing block storage on disk when memory is insufficient, including its initialization and the creation of local directories for storage.
- Explains the logic behind creating a two-tier directory structure for efficient file storage and access.

### 4.5 DiskStore

- Details on how DiskStore comes into play when MemoryStore cannot accommodate blocks due to limited space, allowing blocks to be stored on disk.
- Discusses methods for reading from and writing to disk using NIO, ensuring data persistence beyond the lifetime of Spark tasks.

### 4.6 MemoryStore

- Focuses on MemoryStore's responsibilities for storing blocks in memory, including the mechanism for safe unrolling of blocks to avoid memory overflow.
- Introduces concepts like unroll memory, and strategies for freeing memory when needed, ensuring efficient memory usage.

### 4.7 TachyonStore

- Introduces Tachyon, a distributed file system used by Spark for off-heap data storage, offering high fault tolerance and efficient data sharing across different Spark tasks and stages.
- Explains how Tachyon fits into Spark's ecosystem, providing a layer between compute frameworks and storage systems to enhance performance and data management.

### 4.8 BlockManager Detailed Implementation

- Dives deeper into the functionalities of BlockManager, including methods for dropping blocks from memory, ensuring free space, and handling block storage across memory, disk, and Tachyon stores.

This chapter provides a comprehensive overview of Spark's storage system, illustrating how Spark optimizes data storage and access to improve performance in distributed computing scenarios.
