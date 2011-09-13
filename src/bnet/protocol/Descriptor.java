// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: lib/protocol/descriptor.proto

package bnet.protocol;

public final class Descriptor {
  private Descriptor() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class Path extends
      com.google.protobuf.GeneratedMessage {
    // Use Path.newBuilder() to construct.
    private Path() {
      initFields();
    }
    private Path(boolean noInit) {}
    
    private static final Path defaultInstance;
    public static Path getDefaultInstance() {
      return defaultInstance;
    }
    
    public Path getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return bnet.protocol.Descriptor.internal_static_bnet_protocol_Path_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return bnet.protocol.Descriptor.internal_static_bnet_protocol_Path_fieldAccessorTable;
    }
    
    // repeated uint32 ordinal = 1;
    public static final int ORDINAL_FIELD_NUMBER = 1;
    private java.util.List<java.lang.Integer> ordinal_ =
      java.util.Collections.emptyList();
    public java.util.List<java.lang.Integer> getOrdinalList() {
      return ordinal_;
    }
    public int getOrdinalCount() { return ordinal_.size(); }
    public int getOrdinal(int index) {
      return ordinal_.get(index);
    }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int element : getOrdinalList()) {
        output.writeUInt32(1, element);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      {
        int dataSize = 0;
        for (int element : getOrdinalList()) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeUInt32SizeNoTag(element);
        }
        size += dataSize;
        size += 1 * getOrdinalList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static bnet.protocol.Descriptor.Path parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static bnet.protocol.Descriptor.Path parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static bnet.protocol.Descriptor.Path parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static bnet.protocol.Descriptor.Path parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(bnet.protocol.Descriptor.Path prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private bnet.protocol.Descriptor.Path result;
      
      // Construct using bnet.protocol.Descriptor.Path.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new bnet.protocol.Descriptor.Path();
        return builder;
      }
      
      protected bnet.protocol.Descriptor.Path internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new bnet.protocol.Descriptor.Path();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return bnet.protocol.Descriptor.Path.getDescriptor();
      }
      
      public bnet.protocol.Descriptor.Path getDefaultInstanceForType() {
        return bnet.protocol.Descriptor.Path.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public bnet.protocol.Descriptor.Path build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private bnet.protocol.Descriptor.Path buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public bnet.protocol.Descriptor.Path buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.ordinal_ != java.util.Collections.EMPTY_LIST) {
          result.ordinal_ =
            java.util.Collections.unmodifiableList(result.ordinal_);
        }
        bnet.protocol.Descriptor.Path returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof bnet.protocol.Descriptor.Path) {
          return mergeFrom((bnet.protocol.Descriptor.Path)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(bnet.protocol.Descriptor.Path other) {
        if (other == bnet.protocol.Descriptor.Path.getDefaultInstance()) return this;
        if (!other.ordinal_.isEmpty()) {
          if (result.ordinal_.isEmpty()) {
            result.ordinal_ = new java.util.ArrayList<java.lang.Integer>();
          }
          result.ordinal_.addAll(other.ordinal_);
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 8: {
              addOrdinal(input.readUInt32());
              break;
            }
            case 10: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addOrdinal(input.readUInt32());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      }
      
      
      // repeated uint32 ordinal = 1;
      public java.util.List<java.lang.Integer> getOrdinalList() {
        return java.util.Collections.unmodifiableList(result.ordinal_);
      }
      public int getOrdinalCount() {
        return result.getOrdinalCount();
      }
      public int getOrdinal(int index) {
        return result.getOrdinal(index);
      }
      public Builder setOrdinal(int index, int value) {
        result.ordinal_.set(index, value);
        return this;
      }
      public Builder addOrdinal(int value) {
        if (result.ordinal_.isEmpty()) {
          result.ordinal_ = new java.util.ArrayList<java.lang.Integer>();
        }
        result.ordinal_.add(value);
        return this;
      }
      public Builder addAllOrdinal(
          java.lang.Iterable<? extends java.lang.Integer> values) {
        if (result.ordinal_.isEmpty()) {
          result.ordinal_ = new java.util.ArrayList<java.lang.Integer>();
        }
        super.addAll(values, result.ordinal_);
        return this;
      }
      public Builder clearOrdinal() {
        result.ordinal_ = java.util.Collections.emptyList();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:bnet.protocol.Path)
    }
    
    static {
      defaultInstance = new Path(true);
      bnet.protocol.Descriptor.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:bnet.protocol.Path)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_bnet_protocol_Path_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_bnet_protocol_Path_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\035lib/protocol/descriptor.proto\022\rbnet.pr" +
      "otocol\"\027\n\004Path\022\017\n\007ordinal\030\001 \003(\r"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_bnet_protocol_Path_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_bnet_protocol_Path_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_bnet_protocol_Path_descriptor,
              new java.lang.String[] { "Ordinal", },
              bnet.protocol.Descriptor.Path.class,
              bnet.protocol.Descriptor.Path.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
