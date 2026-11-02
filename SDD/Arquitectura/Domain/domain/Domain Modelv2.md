Domain Model
1. Purpose
The Domain Model describes the business concepts, responsibilities, relationships, and invariants of NexusMarket. It is the conceptual basis for implementing the domain layer and must remain consistent with the functional business specification.

The model distinguishes:
- Entities, whose identity remains stable while their state changes.
- Value Objects, which have no independent identity and are defined by their values.
- Domain services, which coordinate domain operations that do not naturally belong to one entity.
- Domain rules, which must be enforced regardless of the interface or persistence mechanism.
Traceability: Statements explicitly supported by the functional specification are treated as requirements. Modeling choices that organize those requirements are design proposals. Unspecified business policies remain pending decisions.

2. Domain Class Hierarchy
User (abstract)
├── Buyer
├── Seller
├── LogisticsOperator
├── Administrator
└── Supervisor

Product (abstract)
├── PhysicalProduct
└── DigitalProduct

ProductVariant
Warehouse
InventoryStock
InventoryMovement
ShoppingCart
CartItem
CustomerOrder
OrderItem
Payment
Invoice
Shipment
ReturnRequest
Refund
ProductVariant is proposed to represent a purchasable configuration (for example, size or color). The need for variants is present in the current model; the exact variant attributes depend on product requirements.
3. Entities and Responsibilities
3.1 User (abstract)
Purpose: Represents a person authorized to participate in NexusMarket.

Attributes
- userId: UserId
- identification: IdentityDocument
- fullName: PersonName
- email: EmailAddress
- status: UserStatus
Relationships
- One User has exactly one role, represented by its concrete subtype in this model.
- A Buyer owns a ShoppingCart and may create CustomerOrders.
- A Seller manages its own Products.
- An Administrator registers Sellers and their initial Warehouse according to the specification.
- A LogisticsOperator performs authorized warehouse and shipment operations.
- A Supervisor consults marketplace information according to assigned permissions.
Rules
- Identification and email must be unique in the system (uniqueness requires application/persistence coordination).
- User cannot be created with invalid identity/contact values.
- User status changes must use controlled domain operations.
- Authentication and authorization are application/security responsibilities; the domain role does not itself authenticate a user.
3.2 Buyer
Purpose: Represents a customer who purchases products.

Attributes
- mainAddress: Address
- additionalAddresses: List<Address>
- commercialStatus: CommercialStatus
Relationships
- One Buyer owns one ShoppingCart (proposed one-cart-per-buyer policy).
- One Buyer may own many CustomerOrders.
Rules
- Main address is mandatory if the functional flow requires delivery; confirm whether digital-only buyers may omit it.
- Additional addresses are optional.
- A Buyer cannot manage another buyer's data or inventory.
- Purchase eligibility depends on commercial status; the allowed statuses and their effects are pending business definition.
3.3 Seller
Purpose: Represents a participant who commercializes products.

Relationships
- One Seller manages zero or more Products.
- Seller may be associated with Seller-type Warehouses.
Rules
- Seller self-registration is not allowed.
- Seller is incorporated by an Administrator.
- Seller can manage only its own products and permitted commercial data.
- Seller's first warehouse registration is performed by the Administrator, as described in the specification.
3.4 LogisticsOperator
Purpose: Executes physical logistics operations.

Relationships
- Performs operations on Warehouses and Shipments.
Rules
- Operations must be authorized for the role.
- A logistics operation cannot bypass stock, order, or shipment state rules.
3.5 Administrator
Purpose: Performs marketplace administration.

Relationships
- Registers Sellers.
- Registers the Seller's first Warehouse.
Rules
- Seller registration and initial warehouse registration must follow the specified administrative flow.
3.6 Supervisor
Purpose: Consults marketplace information and monitors operations.

Rules
- Consultation does not imply permission to modify operational data.
- Exact reports and visibility are pending definition.
3.7 Product (abstract)
Purpose: Represents an item offered in the catalog.

Attributes
- productId: ProductId
- name: ProductName
- description: ProductDescription
- seller: Seller
- status: ProductStatus
Relationships
- One Product belongs to one Seller.
- One Product may have one or more ProductVariants.
- Physical products have stock records; digital products do not require physical stock.
- Product status controls whether it can be offered for purchase.
Rules
- Product cannot be instantiated directly.
- Product status transitions must be controlled.
- A product must have a valid seller and required descriptive/commercial data.
- Physical and digital fulfillment behavior differs.
3.8 PhysicalProduct
Purpose: Product requiring physical stock and delivery.

Rules
- Purchase fulfillment requires available stock to be reserved/confirmed.
- Physical fulfillment requires shipment.
- Damaged or unavailable stock cannot be reserved.
3.9 DigitalProduct
Purpose: Product delivered digitally.

Rules
- Does not require physical inventory or shipment.
- Delivery is triggered after payment confirmation.
- The actual digital-delivery mechanism/content is outside the current domain definition and remains pending.
3.10 ProductVariant
Purpose: Represents a purchasable configuration of a Product.

Proposed attributes
- variantId: ProductVariantId
- sku: SKU
- attributes: product-specific characteristics
- price: Money
- status: ProductStatus or a dedicated VariantStatus (decision pending)
Rules
- A variant belongs to exactly one Product.
- SKU must be unique (system-wide or per seller: pending decision).
- A cart/order should reference a variant when variants affect price, stock, or fulfillment.
- Do not use List<String> as a substitute for a structured purchasable variant.
3.11 Warehouse
Purpose: Physical location used to store stock.

Attributes
- warehouseId: WarehouseId
- name: WarehouseName
- type: WarehouseType
- seller: Seller (nullable only for Marketplace warehouse)
Rules
- Warehouse type is controlled.
- Seller-type Warehouse must be associated with a Seller.
- Marketplace Warehouse must not be associated with an individual Seller.
- The precise warehouse ownership/cardinality rules must be confirmed against the specification.
3.12 InventoryStock
Purpose: Maintains the current stock balance for a variant in a warehouse.

Attributes
- stockId: StockId (or composite identity Variant + Warehouse)
- variant: ProductVariant
- warehouse: Warehouse
- availableQuantity: Quantity
- reservedQuantity: Quantity
- damagedQuantity: Quantity (include only if damaged stock is managed explicitly)
Rules
- Quantities cannot be negative.
- Available stock cannot be reserved beyond the available balance.
- Damaged stock is not available for reservation.
- A stock record is unique for a Variant + Warehouse combination.
- Keep stock balance separate from movement history.
3.13 InventoryMovement
Purpose: Records each stock operation for traceability.

Attributes
- movementId: InventoryMovementId
- stock: InventoryStock
- type: InventoryMovementType
- quantity: Positive Quantity
- occurredAt: Instant
- reference: optional business reference (order, return, adjustment)
Rules
- Movement quantity must be positive.
- Movement type must be controlled.
- The movement and balance update must be consistent/atomic.
- Reservation, sale output, adjustment, entry, and return must follow their respective rules.
- A movement is a historical record; it is not the current stock balance.
3.14 ShoppingCart
Purpose: Holds a Buyer's provisional selection.

Attributes
- cartId: CartId
- buyer: Buyer
- items: List<CartItem>
Rules
- Cart belongs to one Buyer.
- Cart cannot contain invalid quantities.
- Adding a product requires it to be eligible for sale.
- Cart is not an Order and does not use OrderStatus.
- Checkout must validate current price, status, and stock; cart contents alone do not guarantee availability.
3.15 CartItem
Purpose: Represents a variant and quantity selected in a cart.

Attributes
- variant: ProductVariant
- quantity: Quantity
Rules
- Quantity must be greater than zero.
- Repeated selection of the same variant should update quantity or be consolidated (implementation policy).
3.16 CustomerOrder
Purpose: Represents a confirmed commercial purchase.

Attributes
- orderId: OrderId
- buyer: Buyer
- items: List<OrderItem>
- status: OrderStatus
- total: Money (calculated from immutable item snapshots; not freely set)
- createdAt: Instant
Relationships
- One order belongs to one Buyer.
- One order contains one or more OrderItems.
- One order has payment records/attempts.
- One order may have an Invoice.
- Physical items require shipment; digital items are delivered after payment.
- One order may be subject to return requests.
Rules
- Order is created from a validated cart/checkout, not from a cart status.
- Order lines preserve the product/variant, quantity, and price at purchase time.
- Totals are calculated; callers cannot arbitrarily assign them.
- State transitions must be explicit and valid.
- A completed order cannot be modified.
- Payment confirmation precedes fulfillment.
- Multi-seller orders and split shipments are pending policy decisions.
3.17 OrderItem
Purpose: Preserves the purchase detail as it was agreed at checkout.

Attributes
- productId/product reference
- variantId/variant reference (when applicable)
- productNameSnapshot
- skuSnapshot
- unitPrice: Money
- quantity: Quantity
- lineTotal: Money (calculated)
Rules
- Quantity is positive.
- Unit price is captured at checkout and does not change if catalog price changes later.
- Line total is calculated from unit price and quantity.
- OrderItem is not a live copy of mutable catalog data.
3.18 Payment
Purpose: Records financial confirmation associated with an order.

Proposed attributes
- paymentId: PaymentId
- order: CustomerOrder
- amount: Money
- status: PaymentStatus
- paidAt: optional Instant
- transactionReference: optional reference from payment provider
Rules
- Payment amount must be positive.
- An order cannot be treated as paid without confirmed payment.
- Payment confirmation should be idempotent (same confirmation must not duplicate effects).
- Gateway, failed attempts, expiry, and cancellation policies are pending.
3.19 Invoice
Purpose: Represents billing information associated with an order.

Rules
- Invoice references one order.
- Invoice data and tax/legal requirements are pending definition.
- Do not assume that an invoice is legally equivalent to a Colombian electronic invoice until requirements/provider are specified.
3.20 Shipment
Purpose: Tracks physical delivery of order items.

Proposed attributes
- shipmentId: ShipmentId
- order: CustomerOrder
- status: ShipmentStatus
- destination: Address
- trackingNumber: optional TrackingNumber
- dispatchedAt: optional Instant
- deliveredAt: optional Instant
Rules
- Shipment applies only to physical fulfillment.
- Dispatch requires payment confirmation and fulfillment preparation.
- Delivery confirmation must follow valid shipment states.
- Carrier, shipping costs, split shipments, and tracking integration are pending.
3.21 ReturnRequest
Purpose: Represents a request to return purchased items.

Proposed attributes
- returnId: ReturnId
- order: CustomerOrder
- items: returned order lines and quantities
- status: ReturnStatus
- requestedAt: Instant
- reason: ReturnReason
Rules
- Return quantity cannot exceed the quantity purchased minus already returned quantities.
- Return must refer to an existing order item.
- Return eligibility period, conditions, and approval policy are pending.
- A return does not automatically imply a refund or stock re-entry.
3.22 Refund
Purpose: Records money returned to a Buyer.

Proposed attributes
- refundId: RefundId
- payment: Payment
- returnRequest: ReturnRequest (optional only if policy permits non-return refunds)
- amount: Money
- status: RefundStatus
- processedAt: optional Instant
Rules
- Refund amount must be positive and cannot exceed the refundable amount.
- Refund processing must be recorded and idempotent.
- Refund approval and payment-provider policies are pending.
4. Domain Relationships and Cardinalities
Buyer 1 ───── 1 ShoppingCart
Buyer 1 ───── 0..* CustomerOrder
Seller 1 ──── 0..* Product
Product 1 ──── 1..* ProductVariant (proposed; confirm products without variants)
ProductVariant 1 ── 0..* InventoryStock
Warehouse 1 ── 0..* InventoryStock
InventoryStock 1 ── 0..* InventoryMovement
ShoppingCart 1 ── 0..* CartItem
CartItem * ─── 1 ProductVariant
CustomerOrder 1 ── 1..* OrderItem
OrderItem * ── 1 ProductVariant (or product reference for non-variant model)
CustomerOrder 1 ── 0..* Payment
CustomerOrder 1 ── 0..1 Invoice (proposed)
CustomerOrder 1 ── 0..* Shipment (policy pending)
CustomerOrder 1 ── 0..* ReturnRequest
ReturnRequest 1 ── 0..* Refund (policy pending)
These cardinalities are modeling proposals where the specification does not define them explicitly.
5. Order Lifecycle
Checkout (cart, not order state)
   │ validates products, prices, buyer and stock
   ▼
PENDING_PAYMENT
   │ confirmed payment
   ▼
PAID
   │ physical fulfillment
   ▼
DISPATCHED
   │ delivery confirmed
   ▼
DELIVERED / COMPLETED
For digital products, after payment confirmation the system performs digital delivery; physical dispatch is not applicable. The exact order status for digital completion and mixed orders requires a policy decision.

Important correction: CART must not be an OrderStatus. A cart is a separate entity and an order begins when checkout creates it.
6. Domain Invariants (must be protected)
1. Identity, email, SKU and other unique values are validated for format in the domain; uniqueness across records is enforced with application/persistence coordination.
2. Quantities are positive for cart/order/movement lines.
3. Stock balances never become negative.
4. Reserved stock cannot exceed available stock.
5. Damaged stock cannot be reserved.
6. Product cannot be purchased when not published/eligible.
7. Checkout validates price, product status and stock at the moment of confirmation.
8. Order item prices are snapshots.
9. Order total is calculated from order items.
10. Order state transitions are restricted.
11. Payment confirmation cannot be duplicated.
12. Physical products require physical fulfillment; digital products do not require shipment.
13. Completed orders are immutable except for explicitly modeled post-sale processes.
14. A return cannot exceed the eligible quantity purchased.
15. A refund cannot exceed the refundable amount.
16. Seller operations are limited to seller-owned products/data.
17. Role authorization is enforced in the application/security layer and cannot be replaced by trusting a request field.
7. Domain Services (candidate responsibilities)
Services are proposed for operations involving multiple entities or coordination:
- RegisterSellerService: creates Seller and initial Warehouse under Administrator flow.
- AddProductToCartService: validates product eligibility and updates cart.
- CheckoutService: validates cart, prices, buyer and stock; creates order snapshots and reservations according to policy.
- ConfirmPaymentService: records confirmation and triggers the correct fulfillment path.
- ReserveInventoryService: reserves stock safely.
- DispatchOrderService: validates paid order and prepares shipment.
- ConfirmDeliveryService: validates shipment delivery and completes the order.
- RequestReturnService: validates return eligibility and quantities.
- ProcessRefundService: validates refundable amount and records refund.
- AdjustInventoryService: records adjustment and updates stock consistently.
Service names and granularity will be refined against the professor's use-case/service style before implementation.
8. Decisions Still Pending
These must be resolved from the specification, professor, or team—not guessed:
- Buyer self-registration.
- Whether an order can contain products from multiple sellers.
- Whether one order can have multiple shipments.
- Stock reservation timing and reservation expiration.
- Payment gateway, failure, retry, expiry and cancellation behavior.
- Cancellation before/after payment.
- CommercialStatus allowed values and their effects.
- Whether variants are mandatory for every product and SKU uniqueness scope.
- Invoice fields, tax and electronic invoicing requirements.
- Shipping costs, carrier and delivery confirmation evidence.
- Return period, conditions, approval and restocking policy.
- Refund approval and partial refunds.
- Reports and Supervisor visibility.
- Digital content delivery mechanism.
- Non-functional requirements (security, audit, availability, performance).
9. Modeling and Implementation Rules
- Keep entities responsible for their own invariants and state transitions.
- Do not expose unrestricted setters for invariant-sensitive state.
- Use Value Objects for validated concepts such as Money, Quantity, EmailAddress, Address, SKU, and identifiers.
- Use controlled catalogs/enums for fixed domain states.
- Keep domain classes independent of Spring, JPA, MongoDB and HTTP.
- Persistence mapping must not dictate the domain model.
- Test domain rules without starting Spring.
- Do not create classes solely to wrap a primitive; a Value Object must have validation or semantic value.
10. Traceability and Status of Decisions
Element	Classification
User roles and product types	Functional specification
Seller cannot self-register	Functional specification
Physical stock/dispatch vs digital delivery	Functional specification
Inventory cannot be negative	Functional specification
Completed order is immutable	Functional specification
ProductVariant, CartItem, OrderItem, Payment as explicit classes	Modeling proposal to support rules and traceability
InventoryStock separated from InventoryMovement	Modeling proposal for accurate stock/history
One cart per Buyer	Assumption; confirm
Invoice cardinality and shipment splitting	Pending policy
Return/refund conditions	Pending policy