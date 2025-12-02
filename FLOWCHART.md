```mermaid
graph TD
    A[Frontend] -->|HTTP Request| B(Backend API)
    B -->|Database Query| C{Database}
    C -->|Query Result| B
    B -->|HTTP Response| A
```
